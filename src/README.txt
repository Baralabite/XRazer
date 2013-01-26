Permissions:

xrazer.alert: Anyone who has this node will be allerted to possible xrayers
xrazer.check: Allows use of /xrazer check command
xrazer.whitelist: Allows use of /xrazer whitelist command
xrazer.version: Allows use of /xrazer version command
xrazer.reload: Allows use of /xrazer reload command

Commands:

/xrazer check <name>
/xrazer whitelist add <name>
/xrazer whitelist remove <name>
/xrazer whitelist list
/xrazer version
/xrazer reload

COnfiguration:

LoggedBlocks: Blocks to watch for. FOrmat: id:data, so Orange wool which has an id of 35
and a data value of 1 would be: 35:1
WhitelistedPlayers: List of players to ignore. It still tracks them, just doesn't report them.
XRayPeriod: The amount of time in minutes between checking.
XRayThreshold: The amount of logged blocks needed to be mined before setting off alarm.

Installation:

Just drop into plugins folder, restart server, and configure! Note, it may trigger an error 
on the first initalization.

Notes:

Once the alert comes up that a person is xraying, it resets their counter - so using /xrazer check
straight after an alert wouldn't be of much use.

The "Total" value at the end of an alert message is the total of logged blocks mined.