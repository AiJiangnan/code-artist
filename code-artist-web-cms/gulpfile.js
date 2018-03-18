var gulp = require('gulp'), //gulp基础库
    minifycss = require('gulp-minify-css'), //css压缩
    rename = require('gulp-rename'), //文件重命名
    uglify = require('gulp-uglify'); //js压缩

gulp.task('default', () => {
    gulp.start('minifycss', 'minifyjs');
});

//css压缩
gulp.task('minifycss', () => {
    const path = 'css/easyui';
    return gulp.src(path + '/*.css') //设置css
        .pipe(rename({suffix: '.min'})) //修改文件名
        .pipe(minifycss()) //压缩文件
        .pipe(gulp.dest(path)); //输出文件目录
});

//JS压缩
gulp.task('minifyjs', () => {
    const path = 'js/easyui';
    return gulp.src(path + '/*.js') //选择合并的JS
        .pipe(rename({suffix: '.min'})) //重命名
        .pipe(uglify()) //压缩
        .pipe(gulp.dest(path)); //输出 
});