layui.use('jquery', () => {
    const $ = layui.jquery;
    $.post('show', u => {
        $('#name').html(u.realname);
        $.get('user/menu', p => {
            laytplrender(menu, 'menu-view', p);
            elem();
        });
    });
});

const elem = () => layui.use('element', () => {
    const e = layui.element;
    let flag = true;
    e.on('nav(menu)', m => {
        if (hasTab(m[0].id)) {
            e.tabChange('tab', m[0].id);
        } else {
            e.tabAdd('tab', {
                id: m[0].id,
                title: m[0].textContent + ' <i class="layui-icon layui-tab-close" onclick="deleteTab(' + m[0].id + ')">&#x1006;</i>',
                content: '<iframe frameborder="0" scrolling="no" width="100%" src="' + m[0].attributes[1].value + '"></iframe>'
            });
            e.tabChange('tab', m[0].id);
        }
    });
    e.on('tab(tab)', t => {
        const $ = layui.jquery;
        let step = 5;
        if (t.index === 0) {
            let w1 = flag ? 0 : -200;
            let w2 = flag ? 200 : 0;
            let id = setInterval(() => {
                w1 = flag ? w1 - step : w1 + step;
                w2 = flag ? w2 - step : w2 + step;
                $('.layui-side').css('left', w1);
                $('.layui-body').css('left', w2);
                $('.layui-footer').css('left', w2);
                if (flag && (w1 === -200 || w2 === 0) || w1 === 0 || w2 === 200) {
                    flag = !flag;
                    clearInterval(id);
                }
            }, 1);
        }
    });
});

const deleteTab = id => layui.element.tabDelete('tab', id);