export enum Role {
    CUSTOMER = "CUSTOMER",
    ADMIN = "ADMIN"
}

export interface User {
    username: string;
    role: Role;
}