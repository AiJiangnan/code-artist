layui.use('jquery', () => {
    const $ = layui.jquery;
    $.post('show', u => {
        $('#name').html(u.realname);
        $.get('user/menu', p => {
            laytplrender(menu, 'menu-view', p);
            layui.use('element');
        });
    });
});