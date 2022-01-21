# open-car-share
Open source solution to find transport within or between cities. [work in progress, open for PRs, check issues tab]

As a driver I create trip at the specific date and time, e.g. "Kazan - Moscow 7:00 21.11.2022" 

As a driver I can pick up two people on this trip

As a user I can view all trips on specific date and time according my trip location criterias

As a user I can book a trip

As a user I can confirm trip by phone call with driver

# deployment

101. Setup MYSQL to support UTF8 or utf8mb4 encoding (by default latin1 enconding doesn't support cyrillic characters)
102. Check character_set_client and character_set_result enconding are the same by executing this command in mysql console:

show variables like 'character_set%';

+--------------------------+----------------------------+
| Variable_name            | Value                      |
+--------------------------+----------------------------+
| character_set_client     | utf8                       |
| character_set_connection | utf8                       |
| character_set_database   | latin1                     |
| character_set_filesystem | binary                     |
| character_set_results    | utf8                       |
| character_set_server     | latin1                     |
| character_set_system     | utf8                       |
| character_sets_dir       | /usr/share/mysql/charsets/ |
+--------------------------+----------------------------+

