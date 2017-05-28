## Warning

This software is intended to be a proof of concept, and still needs a lot of testing and improving to be considered a final product.
It mostly works if you use it properly, but random crashes and errors can happen. So I only recommend to use it for testing for the moment.

# Zcash Pannel

This is an android app that allows to manage your zcash wallet from your phone. It connects to your computer through an .onion service of the Tor network,
and serves as a front-end for it. Hence, to use it you need the following:

- A computer running the official zcash wallet.
- A .onion service that redirects http requests to the zcash port ( [pyzcto](https://github.com/miguelmarco/pyzcto) can take care of setting it up easily ).
- [Orbot](https://guardianproject.info/apps/orbot/) installed in your android device. It is available in the [play store](https://play.google.com/store/apps/developer?id=The+Guardian+Project) and [FDroid](https://guardianproject.info/fdroid/)
- The android [barcode scanner](https://github.com/zxing/zxing) by zxing installed in your android device. It is not strictly necessary, but without it you would have to enter everything by hand, which can be a pain. It is available in the [play store](https://play.google.com/store/apps/details?id=com.google.zxing.client.android) and [FDroid](https://f-droid.org/repository/browse/?fdid=com.google.zxing.client.android).

## Usage

### First time configuration.

Assuming that you have a computer with the zcash daemon running, and pyzcto connected to it and running a tor instance, you can configure your Zcash Pannel app to use that particular service by going to the "Config" tab, clicking on the "Scan" button, and scanning the qr code that pyzcto shows. You can also manually edit the fields of the onion address, username and password (of the zcash daemon).

### Seeing you balance.

Once configured, you need to fetch your zcash addresses from your zcash wallet. In order to do so, go to the "Balances" tab and click on the "Update" button. It will then connect to your computer through the tor network and get your balances and addresses. It might take a few seconds. Then it will show you your global balances and the balance of each address you have.

### Sending funds.

Once you have your list of addresses, you can go to the "Send" tab and select the address you want to send from. Then fill the address you want to send to, and the amount you want to send (or you can scan a request to fill them automatically). Once it is all filled, you can click the "Send" button. It will show a screen describing the details of the transaction while it is being processed. Once it has been processed it will show you a dialog telling so.

### Receiving funds.

You can create a request for someone to send you a certain amount of zcash. Just go to the "Receive" tab, choose the address you want the funds to be sent, and fill up the amount you want to receive. Click on the "Generate QR" button and it will show a QR code with the request, that can be scanned by the person that wants to send you the funds.

# Acknowledgements

This software wouldn't be possible without the work done by the Tor project and the Guardian project. In partticular, i would like to thank n8fr8 for his help with dealing with the netcipher library.

# Contributing

As I said before, I did this app just as a proof of concept, and currently don't have plans to keep developping or maintaining it. If you think it would be a good idea to polish this code into a really functional and reliable app (or maybe an even better idea would be to rewrite it from scratch, since I am really a newbie in android developping and my code is not good at all), please do so. I would love to see somebody taking this project from me and making it grow.

If you don't want to code, but want to convince me to keep working on improving this app, you can donate some zcash to the following addres (but I don't promise anything, in fact it is very unlikely that i decide to spend more time in this app; it is more likely that I spend that time improving pyzcto).


zcash:zcayBTtUDZRU6rLsXApP3DbLEsxJa9M7WeigUEv1PQd6sodxHAeRgS3vSN4kh9e81r6Y1cngKdhQdTbsRhUnSJqHHeQGpkJ


