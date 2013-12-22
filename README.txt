Iterated function schemes are a good way to generate fractals.

See http://en.wikipedia.org/wiki/Iterated_function_system if you want the
background.

IFSs are easy to geneate in Java, since you can easily paint one bitmap,
transformed by an Affine transform, into another. I first discovered this
in about 2004 when I had to create some software for the Open University
topology course M338. Then I made a nice iteractive program.

This program I just hacked together to generate a christmas-tree-like
fractal to use as the image image on my 2013 Christmas card. So, no
user-interface. All the code is in a single class. If you want to alter
the image, you have to edit the code, compile it, run it, then open the
image in an image viwer program (e.g. your web browser).

Tim Hunt, December 2013.
