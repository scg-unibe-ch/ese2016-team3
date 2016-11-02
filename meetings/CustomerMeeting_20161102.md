# Meeting

## 02.11.2016

### Agenda

- Demo upgrade
- Demo placeAd (if everything works)
- Demo new design

- Validation in Form-class, is this okey (architectural rule fails...)
- Logging: What should be logged after controller action?
- Premium user visible for other users?

### Protocol

- Filter should be initially empty. This means, no values is set - thus 0 for min fields and infinity for max fields
- The layout of the filter form should be fixed (use columns)

Concerning the release of nov 9:

- Release only things which are working
- Create release on github (google how it works)
- Write a changelog that the user can see what has been implemented

Other questions:

- Validation: Report back to architect and argue, why you break the rule
- Logging: Log at least success/error of controller action, further logging depends on context (e.g. log all bids during an auction)
- Premium users: no optical distinction for release of next week