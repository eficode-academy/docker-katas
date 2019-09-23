# Running as Root in a Container

The purpose of this example is to show,
if you mount the filesystem into a container running as root,
the container will have root privileges on the filesystem.

This example runs well in a Google Cloud Shell.

## As a basic user

1. show the current user

    ```shell
    $ id
    uid=1000(ng) gid=1000(ng) groups=1000(ng),4(adm),27(sudo),999(docker)
    ```

1. create a file and show the owner/permissions

    ```shell
    $ touch file1.txt
    $ ls -la
    total 16
    drwxr-xr-x 2 ng   ng   4096 Sep 23 15:50 .
    drwxr-xr-x 7 ng   ng   4096 Sep 23 15:40 ..
    -rw-r--r-- 1 ng   ng      0 Sep 23 15:50 file1.txt
    ```

> `file1.txt` is has
>
> - `user:group` set to your user e.g. `ng:ng`
> - permissions `-rw-r--r--`, where the first
>   - `rw-` is `read, write` for `user`, the next
>   - `r--` is `read` for `group`, and the last
>   - `r--` is `read` for `other`. So,
> - ng (`user`) can `read` and `write` to the file,
> - users in the ng (`group`) can `read`, and
> - everyone on the linux system (`other`) can `read` the file.

## In the same folder

1. start a privileged container that mounts in the current folder

    ```shell
    $ docker run --rm -it -v $(pwd):/mnt/host alpine
    / #
    ```

1. show the current user (we have `uid=0` and are `root`)

    ```shell
    # id
    uid=0(root) gid=0(root) groups=0(root),1(bin),2(daemon),3(sys),4(adm),6(disk),10(wheel),11(floppy),20(dialout),26(tape),27(video)
    ```

1. show the files and permissions in the `/mnt/host` folder
    (we see the file we created before)

    ```shell
    / # cd /mnt/host/
    /mnt/host # ls -la
    total 16
    drwxr-xr-x    2 1000     1000          4096 Sep 23 13:53 .
    drwxr-xr-x    1 root     root          4096 Sep 23 13:42 ..
    -rw-r--r--    1 1000     1000             0 Sep 23 13:50 file1.txt
    ```

1. create a file with some secret content and show the permissions

    ```shell
    /mnt/host # echo "secret" > file2.txt
    /mnt/host # ls -la
    total 12
    drwxr-xr-x    2 1000     1000          4096 Sep 23 14:18 .
    drwxr-xr-x    1 root     root          4096 Sep 23 13:42 ..
    -rw-r--r--    1 1000     1000             0 Sep 23 13:50 file1.txt
    -rw-r--r--    1 root     root             7 Sep 23 14:18 file2.txt
    ```

1. delete the `read` permission for `other` on the file

    ```shell
    /mnt/host # chmod o-r file2.txt
    /mnt/host # ls -la
    total 12
    drwxr-xr-x    2 1000     1000          4096 Sep 23 14:18 .
    drwxr-xr-x    1 root     root          4096 Sep 23 13:42 ..
    -rw-r--r--    1 1000     1000             0 Sep 23 13:50 file1.txt
    -rw-r-----    1 root     root             7 Sep 23 14:18 file2.txt
    /mnt/host #
    ```

1. exit the privileged container, and try viewing the permissions on the files
    (`file2.txt` is owned by `root:root`)

    ```shell
    /mnt/host # exit
    $ ls -la
    total 12
    drwxr-xr-x 2 ng   ng   4096 Sep 23 16:21 .
    drwxr-xr-x 7 ng   ng   4096 Sep 23 15:40 ..
    -rw-r--r-- 1 ng   ng      0 Sep 23 15:50 file1.txt
    -rw-r----- 1 root root    7 Sep 23 16:18 file2.txt
    ```

1. try viewing the contents of the file
    (we can't since `other` doesn't have any permissons on the file)

   ```shell
    $ cat file2.txt
    cat: file2.txt: Permission denied
    ```

1. Use the root user to view the contents of the file

    ```shell
    $ sudo cat file2.txt
    secret
    ```

## Conclusion

Use great caution when mounting the filesystem into a container,
and pay attention to the privileges that a container is running with.

Greatly inspired by the
[Keynote: Running with Scissors - Liz Rice, Technology Evangelist, Aqua Security](https://www.youtube.com/watch?v=ltrV-Qmh3oY) at KubeCon 2018
