import { Pipe, PipeTransform } from "@angular/core";
import { Jersey } from "../jersey";

@Pipe({
    name: 'search'
})
export class SearchPipe implements PipeTransform {
    transform(jerseys: Jersey[], searchTerm: string) {
        searchTerm = searchTerm.toLowerCase();
        return jerseys.filter(jersey => jersey.name.toLowerCase().includes(searchTerm));
    }
}