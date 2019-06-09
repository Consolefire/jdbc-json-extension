const path = require('path');
const merge = require('webpack-merge');
const baseConfig = require('./webpack.base.config.js');
const HtmlWebpackPlugin = require('html-webpack-plugin');


module.exports = merge(baseConfig, {
    devtool: 'eval-source-map',
    mode: process.env.NODE_ENV || 'development',
    plugins: [
        new HtmlWebpackPlugin({
            template: path.join(__dirname, 'templates', 'index-tmpl.html')
        })
    ],

    devServer: {
        inline: true,
        port: '30001',
    },

    
});