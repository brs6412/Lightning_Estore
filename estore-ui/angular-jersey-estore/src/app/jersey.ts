export interface Jersey {
    id: number;
    number: number;
    name: string;
    price: number;
    size: Size;
    color: Color;
    quantity: number;
}

export enum Size {
    SMALL = "Small", 
    MEDIUM = "Medium",
    LARGE = "Large"
}

export enum Color {
    BLUE = "Blue",
    WHITE = "White",
    BLACK = "Black"
}