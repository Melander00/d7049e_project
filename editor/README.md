# Development

For the ui we use Vite + React and Electron as the runtime. 
For development first start the vite server

```bash
npm run ui
```

Start a typescript compiler with 

```bash
npm run watch
```

Run the electron application with:

```bash
npm run dev
```

Note that it starts nodemon which watches for reload. For testing, instead start with 

```bash
npm run start
```

Otherwise you will lose state whenever you save a file.