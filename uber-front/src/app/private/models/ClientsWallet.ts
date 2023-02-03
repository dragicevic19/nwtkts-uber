import { Transaction } from "./Transaction";

export interface ClientsWallet {
    currentBalance: number;
    spentThisMonth: number;
    spentThisYear: number;
    transactionHistory: Transaction[];
}