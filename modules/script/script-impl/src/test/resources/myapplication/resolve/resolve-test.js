var assert = Java.type('org.junit.Assert');

require('other/resolve-in-require').test();
require('other/resolve-in-require').test();

var relative = resolve('./test.html').toString();
assert.assertEquals('myapplication:/resolve/test.html', relative);

var absolute = resolve('/test.html').toString();
assert.assertEquals('myapplication:/site/test.html', absolute);
