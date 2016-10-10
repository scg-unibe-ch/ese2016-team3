# Question list

## Functional requirements

### Requirement 1

> A user can put an ad to sell properties directly or through an auction.

### Requirement 2

> A user can search for properties for sale similarly to the search form for properties for rent

- Is this a seperate function or is it a choice in the existing search? 
- If in existing search, do you just check a new box saying "for sale" or "for rent"? Should you be able to look for both at once?
- Or should there be two search buttons, one for sale and one for rent? Dropdown menu from search button?
- How similar is similar? Are all the other criterias the same, just with total price instead of monthly rent?
- If there are other criterias, what are they?

### Requirement 3

> A user can create search alerts which notify him about new ads corresponding the search criteria.

 - Müssen alle Kritieren zurteffen damit der User von neuen Anzeigen benachrichtigt wird? Oder reicht es auch aus, wenn einige Kritieren zutreffen andere aber nicht.( Bsp: 5 Kritieren treffen zu aber eine nicht)
 - Wie wird der User von den neuen entsprechenden Anzeigen benachrichtig? 
Von wem wird er benachrichtig? 
Bekommt der User eine Email vom System?
- Welches sind diese "search criterias"? Bsp: Ort, Mietpreis, Tierfreundlich, Balkon, Lift, etc...

### Requirement 4

> Extend the search capabilities to cover more filter criteria

### Requirement 5

> Different user roles: Premium and Normal
> 
> - Premium users: get alerts and results early
> - Normal users get the results a bit late
> - The adds of premium user are a bit higher in the result list

- What is meant by early? Immediately?
- What is meant by results?
- What is meant by late? A few hours, a few days?
- How should the ads of premium users be displayed in the list? Simply at the top of the list or should they be highlighted?

## Non-functional requirements

### Design

- Should the design be responsive? (e.g. using bootstrap or something similar)