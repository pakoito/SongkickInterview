(De)reference Songkick 2015
==========

(De)reference Songkick is an interview project branched off my own reference app. It allows the user to search for artists.

The debug version includes LeakCanary to detect memory issues, and a debug drawer accessible by swiping from the right edge with information about the state of the application and the device.

Code related to the interview can be found at the following packages:

* everything under screens.songkicklist and screens.songkickdetails
* api configuration in dependencies.meodules.NetworkModule and network.SongkickApi
* ui elements under ui.delegates and ui.adapters
* api related POJOs under model

## Architecture

It is a reference app applying most architectural patterns necessary to have a configurable, testable, simple, and calmed development process.

Focus is in doing parts of the heavy lifting via inheritance so every new Activity and its presenter only require code related to their features: dependency tree and MV*. Activities are lightly coupled to the presenter, but presenters can be tested on the JVM if no Android dependencies are referenced. Expect some arcanisms, specially on the Dagger side, to get hierarchical injection while respecting decoupling and the lifecycle.

Encapsulation is not always respected to allow for easier testing. Generally any method returning an Observable is accessible for testing.

Immutability and purity applied where possible, up to Java and the libraries' limitations.

No enums.

## Behaviour

The app observes network connectivity and displays a banner if it's offline.

Upon resuming from cold or background, it checks whether connection is available. If it isn't it tries to fetch the information from latest in-memory cache. If it is it checks whether the info in-memory is "fresh", and if it isn't it goes to network.

The in-memory cache survives rotation, same as the images'. The freshness policy is not being older than 60 seconds.

## Screenshot

See screenshot.png

## Apis

[Artist Search](https://www.songkick.com/developer/artist-search)

[Similar Artist](https://www.songkick.com/developer/similar-artists)

## Known Issues

Network operations persist on rotation only if they hit the network cache. 
While it's intentional to not hinder readability, it can be easily solved by subscribing network calls until destruction instead of until pause.

Ugly iconography.

In debug versions, LeakCanary takes memory dumps if too many images load from network in parallel, which slows the application.

In debug versions, LeakCanary reports a false positive caused by debugdrawer.

## Todo

Tests, tests, tests.

Database caching of responses in CouchDB.

License
==========

Copyright (c) pakoito 2015

GNU GENERAL PUBLIC LICENSE

Version 3, 29 June 2007

See LICENSE.md
