# Meeting

#### 02.11.2016

### Agenda

- Demo upgrade
- Demo placeAd (if everything works)
- Demo new design
- Validation in Form-class, is this okey (architectural rule fails...)
- Logging: What should be logged after controller action?
- Premium user visible for other users?

### Protocol

##### General Requirements for release 

- Everything, which is part of the page must work (no dead links etc.)
- The release should be done with GitHub, which has a specific function for software releases
- A changelog with new features has to be made (in Github)

##### Search (Results)

- The from / to search options have to be changed, so that the fields are empty by default. For example, if a user specifies 3 for min number of rooms and leaves the max field open, only search results with more than 3 rooms should be displayed
- The layout should be changed 

##### Premium users

- In the next release, the distinction between premium and normal users must not yet be implemented
- For the final release, the status of premium users should be visible

##### Architectural Rules

- If architectural rules are broken for good reasons, we can propose deviations from the rules to our code reviewer (and put Haidar in CC)

##### Logging

- The responses of the server to requests have to be written in the log file (after controller action).
- The responses of the server can be represented with status codes (like 404 not found)