This directory should be used for any pre-built forms you don't put in custom.
For example, if your list of pre-built forms gets too big and you have to go
to a javascript or applet based system, the resulting jsps should be put here.

This is due to the assumption in the generated jsps that the most of the resources
needed, such as css and js files, are one directory up.  It just makes life a lot
less complicated in the long run to make that assumption.

I also recommend you keep your xml files in an xml directory under here so you can
regenerate your forms at will.  Email me at mrald-dev-list@lists.mitre.org for
an ant build.xml file to enable you to do this easily.

Jeff
