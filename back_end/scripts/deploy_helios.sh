#!/bin/sh

# sshpass -f /home/huji/.helios_pass scp -P 2222 -r ../front_end/static_content/* static_content/*/* s389491@helios.cs.ifmo.ru:~/www/
sshpass -f /home/huji/.helios_pass scp -P 2222 -r conf/httpd.conf s389491@helios.cs.ifmo.ru:~/httpd-root/conf/
sshpass -f /home/huji/.helios_pass scp -P 2222 -r build/libs/*.jar s389491@helios.cs.ifmo.ru:~/httpd-root/fcgi-bin/