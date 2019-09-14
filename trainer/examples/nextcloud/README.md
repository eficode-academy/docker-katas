# Nextcloud showcase

Spin it up, and create an admin account.
Upload an image to the server, to prove that you have persistent storage.
Make a docker-compose down + up to show that the containers are disposable.
Down them and change nextcloud:11 to :12
Up them and observe that app dies right after start.
Append “depends_on:” annotation
Up them again to show that it upgrades and runs

You can show them the version under the gear icon.
This compose file is missing a network directive- but that is ok. Docker will create a network for these containers and connect them on that network.
