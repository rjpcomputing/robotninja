# Purpose #

---

This article explains how to setup and install Bluetooth on a PC. It also explains how to pair with the LEGO NXT brick.

# Connecting via Bluetooth #

---

## Linux ##
  1. Install Bluetooth packages:
```
$ sudo apt-get install bluez-utils bluez-gnome libbluetooth-dev
```
  1. Make sure Bluetooth on your computer is turned on, then start the bluetooth applet:
```
$ bluetooth-applet
```
  1. When the Bluetooth icon appears in the system tray we can conect and it will help pair with the NXT.
  1. Download [NXTRC](http://www.scienzaludica.it/files/NXTRC.tgz).
  1. Change to the directory where you downloaded `NXTRC`.
```
$ cd ~/lego/linux/NXTRC
```
  1. Scan for your NXT
```
$ ./NXTRC -s
```
> > You should get an output like this:
```
00:16:53:04:CE:24  NXT
```
  1. Use `NXTRC` to get the info on your NXT. This will start the pairing process. Make sure to use the address you got from the previous step after the `-a`.
```
$ ./NXTRC -a 00:16:53:04:CE:24 -i
```
  1. Enter the pin on the NXT. The default (1234) is fine.
  1. Then enter the same pin (1234) on the Linux box. It should popup from the bluetooth-applet.
  1. This output should look something like this:
```
$ ./NXTRC -a 00:16:53:04:CE:24 -i
=: ** Getting Brick Info

=: Device Name  : NXT
=: BT Address   : 00:16:53:04:CE:24
=: Free space   : 93944
=: Battery Level: 7187mV
=: Firmware Ver : 1.5
=: Protocol Ver : 1.124
```
  1. The NXT and the Linux box are now paired and you can use `NXTRC` to tranfer files, execute programs, and list information from the NXT.

### Optional Commandline method ###
  1. Install Bluetooth packages:
```
$ sudo apt-get install bluez-utils bluez-gnome libbluetooth-dev
```
  1. Make sure Bluetooth on your computer is turned on, then start the services:
```
$ sudo /etc/init.d/bluetooth start
```
  1. Now turn Bluetooth on NXT brick on and let your computer scan for it:
```
$ hciconfig hci0
$ hcitool scan
```
> > You should get something like this:
```
00:16:53:04:20:C9       NXT
```
  1. Create a file `/etc/bluetooth/pin` containing plaintext "1234". (This is the default pin on the NXT, you may change it).
```
$ sdptool add --channel=3 SP
$ rfcomm listen /dev/rfcomm0 3
```
> > The NXT should now be able to find your computer (`Bluetoot>Search`). You need to enter the pin to connect.
  1. To view the communication stream enter:
```
$ cat /dev/rfcomm0 | od -t x1 -w1 -v -Ax
```

Most of this comes from this [website](http://mtc.epfl.ch/courses/ProblemSolving-2007/nxt.html).

## Windows ##
Instructions can be found in the `readme.html` file that comes with the LeJOS download.