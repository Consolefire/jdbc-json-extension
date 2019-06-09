const webpack = require('webpack');
const path = require('path');

const SRC_PATH = path.join(__dirname, 'web');


module.exports = {
    entry: path.join(SRC_PATH, 'index.js'),
    output: {
        path: path.join(__dirname, 'build/static'),
        filename: 'application.bundle.js'
    },
    resolve: {
        modules: [path.resolve(__dirname, 'web'), 'node_modules']
    },
    plugins: [
        new webpack.EnvironmentPlugin([
            'NODE_ENV',
        ]),
    ],
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: ['babel-loader']
            },
            {
                test: /\.(css|scss)$/,
                use: [
                    "style-loader",
                    "css-loader",
                    "sass-loader"
                ]
            },
            {
                test: /\.(jpg|jpeg|png|gif|mp3|svg)$/,
                loaders: ["file-loader"]
            }
        ]
    }
};

