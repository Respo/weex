
Respo Weex
----

> An experiment to Compile Respo app in ClojureScript into js and run in Weex.

### Run the demo

* Copy http://repo.respo.site/weex/main.js
* Generate [QR code with this page][qr]
* Open [Weex playground](https://weex-project.io/cn/playground.html) by QR code

[qr]: http://www.qr-code-generator.com/

### Develop

To run the demo, you to install Weex playground first. And then Boot.

Run develop mode:

```bash
boot dev!
# wait for a while, compiling
open http://repo.cirru.org/stack-editor/
```

Generate js bundle:

```bash
boot build-advanced
```

Workflow https://github.com/mvc-works/stack-workflow

### License

MIT
