
Respo Weex
----

> An experiment to Compile Respo app in ClojureScript into js and run in Weex.

### Run the demo

* Copy http://repo.respo.site/weex/main.js
* Generate [QR code with this page][qr]
* Open [Weex playground](https://weex-project.io/cn/playground.html) by QR code

[qr]: http://www.qr-code-generator.com/

![](https://camo.githubusercontent.com/11b6ddd7364b6fd42c9efd63939150a950b0de1a/68747470733a2f2f696d672e616c6963646e2e636f6d2f7470732f5442317a424c6150585858585858655858585858585858585858582d3132312d35392e737667)

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
