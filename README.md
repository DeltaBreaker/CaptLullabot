# CaptLullabot
A custom Twitch/Discord bot for: https://www.twitch.tv/captlullaby/

CaptLullabot commands:

	- !discord
	Displays the link to the discord server
	
	- !discordnotif [true/false] (admin)
	Enables/disables the automatic Discord advertisement (default is enabled)
	
	- !discordnotiftime [minutes] (admin)
	Changes the interval for when the Discord advertisement is displayed (default is 30 minutes)
	
	- !uptime
	Displays how long the bot has been running
	
	- !timeouttime [seconds] (admin)
	Changes how long people are automatically timed out for when caught spamming (default is 3 minutes)
	
	- !newpoll (admin)
	Creates a new poll
	
	- !addvote [anything] (admin)
	Adds an option to the current poll
	
	- !startpoll [seconds] (admin)
	Starts the current poll for the desired duration
	
	- !vote [choice]
	Votes for whatever is chosen (can only be done once per person)
	
	- !rock/paper/scissors
	A simple RPS command
	
	- !checkin
	Adds your account to the list of current viewers so the bot knows youre here
	
	- !balance
	Check how many points you have accumulated
	
	- !balanceadd [points] [user] (admin)
	Adds specified number of points to a user
	
	- !balancesub [points] [user] (admin)
	Removes specified number of points from a user
	
	- !notiflive (admin)
	Send a notification to the Discord server that we just went live
	
	- !encounters [true/false] (admin)
	Enables or disables wild encounters
	
	- !encountertime [minutes] (admin)
	Changes the interval between wild encounters
	
	- !catchtime [seconds] (admin)
	Changes how long you have to catch the current pokemon
	
	- !catchrate [% chance] (admin)
	Changes the catch rate for wild pokemon
	
	- !throw
	Attemps to catch the current pokemon
	
	- !pc blank / [user]
	Either tells you how many pokemon you have or tells you what pokemon is in the specified slot (for designated user)
	
	- !trade [user] [offer PC slot #] [request PC slot #]
	Requests a trade with a user for selected Pokemon
	
	- !accept
	Accepts current trade request
	
	- !decline
	Declines current trade request
	
	- !op [user] (admin)
	Adds a user to the operator list
	
	- !deop [user] (admin)
	Removes user from the operator list
	
	- !specs
	Links to the Capt's PC specs
	
	- !setspecs (admin)
	Sets the link for !specs
	
	- setdiscordlink [link] (admin)
	Sets the link for !discord and the automated message
	
	- settwitchlink [link] (admin)
	Sets the link for !notiflive
	
	- !help
	Links to the bot commands
