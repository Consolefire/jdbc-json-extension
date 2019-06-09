const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const merge = require('webpack-merge');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const baseConfig = require('./webpack.base.config.js');

module.exports = merge(baseConfig, {
    mode: process.env.NODE_ENV || 'development',
    output: {
        path: path.join(__dirname, 'build/static'),
        filename: 'application.js'
    },

    
});