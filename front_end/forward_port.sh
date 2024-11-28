#!/bin/sh
# 29043
sshpass -f "/home/huji/.helios_pass" ssh -p 2222 -L 8080:localhost:29043 s389491@helios.cs.ifmo.ru