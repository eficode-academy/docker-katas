# Nextcloud showcase

An example of starting a two-container application using docker compose;  Nextcloud with a MariaDB backend.

We show that the volumes persist killing the containers,
and a version-upgrade of Nextcloud.

Tags: docker-compose, volumes, upgrading

## Part 1: Persistent storage

1. Start the application with:

    ```shell
    docker compose up
    ```

1. Visit Nextcloud in a browser on: [localhost](http://localhost),
and create an admin account.

> NB: It takes ~1 min for `mariadb` to become ready.
> (During a demo, you'll usually talk for this minute about the demo..)
> If you get the error:
>
> ```output
> Fejl:
> Error while trying to create admin user: Failed to connect to the database: An exception occured in driver: SQLSTATE[HY000] [2002]Connection refused
> ```
>
> Wait a little while, fill in the password again, and try to finish the setup again.

## Upload a file

1. Drag a picture or other file from your computer into the UI,
to upload it to your Nextcloud.

1. Stop the services afterwards.
    > If you ran `docker compose` without `-d`,
    > then `ctrl+c` will stop the services.

    ```shell
    docker compose down
    ```

1. Reload the window in the browser; Nextcloud is down.
1. Show that no containers are running with the command:

    ```shell
    docker ps
    ```

## Removing the containers (optional)

1. You can even remove the containers:

    ```shell
    docker compose rm
    ```

1. Show that they're gone with `docker ps -a`.

## Restart Nextcloud

1. Start the application again with,

    ```shell
    docker compose up
    ```

1. Reload your browser window,
Nextcloud should be available again;
you might even still be logged in.

## Persistent storage

1. Notice how the file you uploaded before is still available.

## Part 2: Upgrading Nextcloud

1. Stop the services again, using `docker compose down` or `ctrl+c`.
1. Change the version of the Nextcloud image from `nextcloud:X`
    to `nextcloud:X+1` (swap the commented image-version lines,
    in the `docker-compose.yml`.)

## Start the Application

1. Run `docker compose up` to start the application.

You can find the version under the `gear icon -> help`,
[link](http://localhost/settings/help).

## Downgrading Nextcloud

> Downgrading is not supported in Nextcloud.

1. Try stopping the containers again
with `docker compose down` or `ctrl+c`.
1. Changing the version back to `nextcloud:12`.
1. Starting the application again with `docker compose up`.

Nextcloud will fail to start with the message:

```shell
app_1  | Can't start Nextcloud because the version of the data (12.0.13.2) is higher than the docker image version (11.0.8.1) and downgrading is not supported. Are you sure you have pulled the newest image version?
```

> NB: This composefile is missing a network directive, but that is ok.
> Docker will create a network for these containers
> and connect them on that network.

## Possible issues

### Making Nextcloud fail without `depends_on`

In the original example,
the upgrade would fail without adding the `depends_on: db`
to the docker compose file.
Nicolaj has been unable to reproduce this error on `Docker for Windows 19.03`, and thus the "example of using `depends_on`" is left out of the tutorial.
`depends_on` is however kept in the docker compose file,
so as to not cause any unintentional errors.
