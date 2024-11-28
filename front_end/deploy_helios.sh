#!/bin/sh

sshpass -f /home/huji/.helios_pass scp -P 2222 -r static_content/* static_content/*/* s389491@helios.cs.ifmo.ru:~/www/
clear