package com.nwtkts.uber.controller;

import com.nwtkts.uber.dto.DriversRidesDTO;
import com.nwtkts.uber.dto.NotificationDTO;
import com.nwtkts.uber.dto.RideDTO;
import com.nwtkts.uber.exception.NotFoundException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.dto.HistoryRideDTO;
import com.nwtkts.uber.repository.UserRepository;
import com.nwtkts.uber.dto.HistoryRideDetailsDTO;
import com.nwtkts.uber.dto.HistoryRideDetailsForDriverDTO;
import com.nwtkts.uber.dto.*;
import com.nwtkts.uber.exception.ClientRideAlreadyRatedException;
import com.nwtkts.uber.exception.TimeFrameForRatingRideExpiredException;
import com.nwtkts.uber.model.*;
import com.nwtkts.uber.service.DriverService;
import com.nwtkts.uber.service.RideService;
import com.nwtkts.uber.service.UserService;
import com.nwtkts.uber.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("api/ride")
public class RideController {

    private final RideService rideService;
    private final VehicleService vehicleService;
    private final DriverService driverService;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Autowired
    public RideController(RideService rideService, VehicleService vehicleService,
                          DriverService driverService, UserService userService, SimpMessagingTemplate simpMessagingTemplate) {
        this.rideService = rideService;
        this.vehicleService = vehicleService;
        this.driverService = driverService;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO rideDTO) {
        Vehicle vehicle = this.vehicleService.getVehicleForDriverFake(rideDTO);
        Driver driver = this.driverService.getDriverForVehicle(vehicle);
        if (driver == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Ride ride = this.rideService.createRide(new Ride(rideDTO), vehicle, driver);
        RideDTO returnRideDTO = new RideDTO(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> endRide(@PathVariable("id") Long id) {
        List<Ride> thisAndNextRide = this.rideService.endRide(id);
        Ride ride = thisAndNextRide.get(0);
        RideDTO returnRideDTO = new RideDTO(ride, ride.getClientsInfo());
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", returnRideDTO);
        if (ride.getRideStatus() == RideStatus.WAITING_FOR_CLIENT || ride.getRideStatus() == RideStatus.TO_PICKUP ||
                ride.getRideStatus() == RideStatus.ENDING) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/change-drivers-ride-status", new DriversRidesDTO(ride));
        }
        if (ride.getRideStatus() == RideStatus.ENDED || ride.getRideStatus() == RideStatus.CANCELED) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/driver-ending-ride", new DriversRidesDTO(ride));
        }
        if (thisAndNextRide.size() > 1) {
            this.simpMessagingTemplate.convertAndSend("/map-updates/change-drivers-ride-status",
                    new DriversRidesDTO(thisAndNextRide.get(1)));
        }
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @PutMapping(
            path = "fake/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> endFakeRide(@PathVariable("id") Long id) {
        Ride ride = this.rideService.endFakeRide(id);
        RideDTO returnRideDTO = new RideDTO(ride);
        this.simpMessagingTemplate.convertAndSend("/map-updates/ended-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @GetMapping(
            produces = "application/json"
    )
    public ResponseEntity<List<RideDTO>> getRides() {
        List<Ride> rides = this.rideService.getDetailedRides();
        List<RideDTO> rideDTOs = new ArrayList<>();
        for (Ride ride : rides) {
            rideDTOs.add(new RideDTO(ride, ride.getClientsInfo()));
        }
        return new ResponseEntity<>(rideDTOs, HttpStatus.OK);
    }

    @GetMapping(
            path = "/driver/{id}",
            produces = "application/json"
    )
    public ResponseEntity<RideDTO> getRideForDriver(@PathVariable Long id) { // ovo pozivam iz locusta kad pravim koordinate za pravu voznju
        Ride ride = this.rideService.getDetailedActiveRideForDriver(id);                  // zato /map-updates/new-ride
        if (ride == null) throw new NotFoundException("Ride doesn't exist!");
        RideDTO returnRideDTO = new RideDTO(ride, ride.getClientsInfo());
        this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride", returnRideDTO);
        return new ResponseEntity<>(returnRideDTO, HttpStatus.OK);
    }

    @GetMapping(
            path = "/scheduled",
            produces = "application/json"
    )
    public ResponseEntity<?> checkScheduledRides() {
        List<Ride> scheduledRidesInNext15Minutes = this.rideService.checkScheduledRides();

        for (Ride ride : scheduledRidesInNext15Minutes) {
            if (ride.getRideStatus() == RideStatus.SCHEDULED && ride.getDriver() != null) {
                this.simpMessagingTemplate.convertAndSend("/map-updates/new-ride-for-driver", new DriversRidesDTO(ride));
            }
            if (ride.getRideStatus() == RideStatus.TO_PICKUP) {
                this.simpMessagingTemplate.convertAndSend("/map-updates/change-drivers-ride-status", new DriversRidesDTO(ride));
            }
            String notification = this.rideService.generateNotificationForClientsScheduledRide(ride);
            if (notification != null) {
                this.simpMessagingTemplate.convertAndSend("/map-updates/client-notifications-scheduled-ride-in",
                        new NotificationDTO(notification, ride.getClientsInfo()));
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(
            produces = "text/plain"
    )
    public ResponseEntity<String> deleteAllVehicles() {
        this.rideService.deleteAllRides();
        this.simpMessagingTemplate.convertAndSend("/map-updates/delete-all-rides", "Delete all rides");
        return new ResponseEntity<>("All rides deleted!", HttpStatus.OK);
    }


    //    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_DRIVER')")
    @GetMapping(path = "/history", produces = "application/json")
    public ResponseEntity<Page<HistoryRideDTO>> getClientRides(Principal user, Pageable page, @RequestParam String sort, @RequestParam String order) {
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        List<String> listOfPossibleSortValues = new ArrayList<>(Arrays.asList("startTime", "calculatedDuration", "price"));
        if (!listOfPossibleSortValues.contains(sort)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        List<String> listOfPossibleOrderValues = new ArrayList<>(Arrays.asList("desc", "asc"));
        if (!listOfPossibleOrderValues.contains(order)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Page<Ride> rides = rideService.getAllEndedRidesOfClient(loggedInUser.getId(), loggedInUser.getRoles().get(0).getName(), page, sort, order);
        Page<HistoryRideDTO> returnPage = rides.map(this::convertToHistoryRideDTO);
        return new ResponseEntity<Page<HistoryRideDTO>>(returnPage, HttpStatus.OK);
    }

    private HistoryRideDTO convertToHistoryRideDTO(Ride r) {
        return new HistoryRideDTO(r);
    }


    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping(path = "/details/client", produces = "application/json")
    public ResponseEntity<HistoryRideDetailsDTO> getDetailsForClientRide(Principal user, @RequestParam Long rideId) {
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Ride ride = this.rideService.findRideById(rideId);
        if (ride == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        ClientRide clientRide = this.rideService.findClientRide(rideId, loggedInUser.getId());
        if (clientRide == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        HistoryRideDetailsDTO dto = convertToHistoryRideDetailsDTO(ride, clientRide);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private HistoryRideDetailsDTO convertToHistoryRideDetailsDTO(Ride r, ClientRide clientRide) {
        return new HistoryRideDetailsDTO(r, clientRide);
    }


    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping(path = "/details/driver", produces = "application/json")
    public ResponseEntity<HistoryRideDetailsForDriverDTO> getDetailsForDriverRide(Principal user, @RequestParam Long rideId) {
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Ride ride = this.rideService.findRideById(rideId);
        if (ride == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (!Objects.equals(ride.getDriver().getId(), loggedInUser.getId())) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        List<ClientRide> clientRides = this.rideService.findClientsForRide(rideId);
        if (clientRides == null || clientRides.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        List<User> clients = new ArrayList<>();
        try {
            for (ClientRide cr : clientRides) {
                User client = userService.findById(cr.getClient().getId());
                clients.add(client);
            }
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        HistoryRideDetailsForDriverDTO dto = convertToHistoryRideDetailsForDriverDTO(ride, clients);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private HistoryRideDetailsForDriverDTO convertToHistoryRideDetailsForDriverDTO(Ride ride, List<User> clients) {
        return new HistoryRideDetailsForDriverDTO(ride, clients);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/details/admin", produces = "application/json")
    public ResponseEntity<HistoryRideDetailsForAdminDTO> getDetailsForRideAdmin(Principal user, @RequestParam Long rideId) {
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Ride ride = this.rideService.findRideById(rideId);
        if (ride == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        List<ClientRide> clientRides = this.rideService.findClientsForRide(rideId);
        if (clientRides == null || clientRides.size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        List<User> clients = new ArrayList<>();
        try {
            for (ClientRide cr : clientRides) {
                User client = userService.findById(cr.getClient().getId());
                clients.add(client);
            }
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        HistoryRideDetailsForAdminDTO dto = convertToHistoryRideDetailsForAdminDTO(ride, clients);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private HistoryRideDetailsForAdminDTO convertToHistoryRideDetailsForAdminDTO(Ride ride, List<User> clients) {
        return new HistoryRideDetailsForAdminDTO(ride, clients);
    }


    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PutMapping(path = "/rating", produces = "application/json")
    public ResponseEntity rateRide(Principal user, @RequestBody RideRatingDTO rideRatingDTO) {
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User loggedInUser = this.userService.findByEmail(user.getName());
        if (loggedInUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (!(1 <= rideRatingDTO.getVehicleRating() && rideRatingDTO.getVehicleRating() <= 5)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (!(1 <= rideRatingDTO.getDriverRating() && rideRatingDTO.getDriverRating() <= 5)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


        try {
            rideService.rateRide(loggedInUser, rideRatingDTO);
        } catch (TimeFrameForRatingRideExpiredException | ClientRideAlreadyRatedException e) {
            new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);

    }
}