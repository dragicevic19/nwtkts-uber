import { Content } from "./Content";
import { Pageable } from "./Pageable";
import { Sort } from "./Sort";

export interface RootObject {
    content: Content[];
    pageable: Pageable;
    last: boolean;
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
    sort: Sort;
    first: boolean;
    numberOfElements: number;
    empty: boolean;
  }