# Pachyderm
Pachyderm is a web-based rich-media interactive presentation authoring and publishing system.

__Preliminary Notes__ (Jan02/16)

- Pachyderm is an __old__ project that has been living on SourceForge ..
- Pachyderm is a Java web application built using Eclipse ..
- Pachyderm's basic framework is WebObjects and Wonder ..
- I see other projects called "pachyderm" .. apologies for using an existing name, but I'm afraid that's what this is called ..
- This first commit should be avoided .. it is a wholesale transfer without any testing ..

__Personal Comments__

Some years ago, I was asked to clean up and enhance the Pachyderm v2.1
web application and, when that work was completed, Pachyderm was
advanced to v3.0 to indicate major changes.  Pachyderm v2.1 was the work
of many people, and Pachyderm v3.0 has continued to benefit from
contributions of other people than me, but I've been the 'custodian' for
about five years as I write this.

Pachyderm has been in use around the world and there are probably some
still active users of the application.  Pachyderm has settled down (no
changes in about a year) and I will probably not do more work than
ensure this version, hosted on GitHub, builds and runs correctly.

I moved Pachyderm from `svn` on SourceForge to `git` on GitHub for a
couple of reasons.  One is that my account on SourceForge has been
rendered usable and my efforts to recover it, and my admin rights, have
been a failure.  A more relevent reason is that `git` is the way of the
future, and anyone picking this up (including me) will know `git` better
than `svn`.  The *huge* subversion history has not been transferred ..
go to SourceForge for your masochistic reading pleasure!

I note, again, that this first commit should be avoided.  It is a
wholesale transfer of the most recent SourceForge version checked out to
my development laptop.  Once it's all on GitHub, I will check it out
from there, rebuild it and make any necessary corrections. When that is
done, I will edit this `README.md` to reflect that progress.

Finally, for now, I will also note that this transfer to GitHub was a my
unilateral decision.  I will inform those developers and users that I
know might have an interest (and for whom I have currect contact
information) of my action.  If the transfer to GitHub is problematic in
any practical way (ie: they are actively using SourceForge), we'll
negotiate and/or compromise all the good of all.

____
(Jan03/16)

Anyone who wants to explore this application will need to climb a bit of
a learning curve.  At this point, it is assumed that the explorer is
familiar with WebObjects (a very powerful Apple, originally NeXT, Java
library on which enterprise web applications have been built), and the
Wonder project, a major Open Source enhancement of WebObjects, which is
under active development.  Pachyderm uses Wonder 7 and, therefore, needs
Java 7.
