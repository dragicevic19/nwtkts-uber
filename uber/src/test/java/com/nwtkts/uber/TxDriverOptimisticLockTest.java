package com.nwtkts.uber;

import com.nwtkts.uber.model.*;
import com.nwtkts.uber.repository.DriverRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TxDriverOptimisticLockTest {

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void testOptimisticLockingScenario() throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> future1 = executor.submit(() -> {
            System.out.println("Startovan Thread 1");
            Driver driverToUpdate = driverRepository.findById(2L).orElseThrow();

            driverToUpdate.setAvailable(false);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            Assertions.assertThrows(ObjectOptimisticLockingFailureException.class,() -> driverRepository.save(driverToUpdate));
        });

        executor.submit(() -> {
            System.out.println("Startovan Thread 2");
            Driver driverToUpdate = driverRepository.findById(2L).orElseThrow();
            driverToUpdate.setAvailable(false);
            driverToUpdate.setNextRideId(5L);
            driverRepository.save(driverToUpdate);
        });
        try {
            future1.get();
        } catch (ExecutionException e) {
            System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas ObjectOptimisticLockingFailureException
            throw e.getCause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }


}
