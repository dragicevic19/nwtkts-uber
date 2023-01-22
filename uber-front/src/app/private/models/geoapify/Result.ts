import { Bbox } from "./Bbox";
import { Datasource } from "./Datasource";
import { Rank } from "./Rank";
import { Timezone } from "./Timezone";

export interface Result {
    datasource: Datasource;
    country: string;
    country_code: string;
    state: string;
    county: string;
    city: string;
    postcode: string;
    district: string;
    suburb: string;
    quarter: string;
    street: string;
    housenumber: string;
    lon: number;
    lat: number;
    distance: number;
    result_type: string;
    formatted: string;
    address_line1: string;
    address_line2: string;
    timezone: Timezone;
    rank: Rank;
    place_id: string;
    bbox: Bbox;
  }