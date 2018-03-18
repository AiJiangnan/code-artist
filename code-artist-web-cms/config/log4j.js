const log4jconfig = {
    appenders: {
        console: {type: 'console'},
        file: {type: 'file', filename: 'log/cheese.log'}
    },
    categories: {default: {appenders: ['console', 'file'], level: 'debug'}}
};

module.exports = log4jconfig;