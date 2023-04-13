let todayTodoIds = new Array();

function showSubCategories(e) {
    var id = $(e).val();
    if($(e).attr('aria-expanded')=="true" && getSubCategories[id]==false){
        var url = "/categories/"+id+"/subcategories";
        $.ajax({
            url: url,
            type:"GET",
            cache: false
        })
        .done(function (data, textStatus, jqXHR) {
            const subCateArr = data;
            for (let i = 0; i < subCateArr.length; i++) {
                let subhtml = '<div class="v-center cate">'
                    + '<button class="btn btn-toggle collapsed sub-cate" data-bs-toggle="collapse" '
                    + 'data-bs-target="#sub-cate'+ subCateArr[i].id+'" value='+subCateArr[i].id+'>' + subCateArr[i]["name"] + '</button>'
                    + '<i class="fa-regular fa-pen-to-square fa-sm cate-edit"></i>'
                    + '<i class="fa-regular fa-trash-can fa-sm cate-del"></i>'
                    + '</div>'
                    + '<div class="collapse sub-todo" id="sub-cate' + subCateArr[i]["id"]+ '"></div>';
                $("#cate"+id).html(subhtml);
            }
            getSubCategories[id] = true;
            getTodos(id);
        })
        .fail(function(xhr, textStatus, errorThrown) {
            $("#text").html("오류 발생.<br>")
                .append("오류명: " + errorThrown + "<br>")
                .append("상태: " + textStatus);
        });
    }
}
function getTodos(categoryId){
    var url = "/categories/"+categoryId+"/todos";
    $.ajax({
        url: url,
        type:"GET"
    })
    .done(function (data, textStatus, jqXHR) {
        const todos = data;
        for (let i = 0; i < todos.length; i++) {
            let subhtml = '<ul class="list-unstyled ps-1 ms-4 mb-1 todo">';
            if(todayTodoIds.includes(todos[i]["id"]+"")){  //숫자->문자
                subhtml += '<input type="checkbox" id="todo'+todos[i]["id"]+ '" value="' + todos[i]["id"]+ '" disabled checked>\n';
            }else{
                subhtml += '<input type="checkbox" id="todo'+todos[i]["id"]+ '" value="' + todos[i]["id"]+ '" >\n';
            }
            subhtml += '<label for="todo' + todos[i]["id"]+ '"> ' + todos[i]["name"]+ '</label>'
                + '<i class="fa-regular fa-trash-can fa-sm i del"></i>'
                + '<i class="fa-regular fa-pen-to-square fa-sm i edit"></i> </ul>';
            $("#cate"+categoryId).append(subhtml);
        }
        console.log("선택한 카테고리의 할일 갯수: " + todos.length+", 상태코드: " + jqXHR.status);
    })
    .fail(function(xhr, textStatus, errorThrown) {
        $("#text").html("할일을 불러오는데 오류 발생<br>")
            .append("오류명: " + errorThrown + "<br>")
            .append("상태: " + textStatus);
    });
}
function getTodayTodoIds(){
    $('#today input[type="checkbox"]').each(function() {
        let todoId = $(this).val();
        todayTodoIds.push(todoId);
    });
}
function todayTodoCheck(){ //오늘할일 중 카테고리없는 할일 checked 표시
    $('#noCateZone input[type="checkbox"]').each(function() {
        let todoId = $(this).val();
        if(todayTodoIds.includes(todoId)){
            $(this).prop('checked', true);
            $(this).prop('disabled', true);
        }
    });
}
function todayImportantOn(){
    $('#todayList input:checkbox').each(function() {
        let todoId = $(this).val();
        let result = todaysImp.find(obj => obj.todoId === parseInt(todoId));
        if(result != undefined && result.important){
            $(this).siblings('i.star').removeClass( "fa-regular" );
            $(this).siblings('i.star').addClass( "star-on fa-solid" );
        }
    });
}
$(document).ready(function() {
    getTodayTodoIds();
    todayTodoCheck();
    todayImportantOn();
    addTodayTodosListener();

    $('#today ul div').slice(0,3).css('border', '1px solid #f0a591');

    $('#today ul div:nth-child(1)').append('<i class="fa-solid fa-1 fa-sm p1"></i>');

    $("#add").click(function() {  //추가
        var checkedTodos = $('input[type=checkbox]:checked:not(:disabled)').map(function() {
            return this.value;
        }).get();
        $.ajax({
            url: '/todos/today',
            type: 'POST',
            data: JSON.stringify(checkedTodos),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (data) {
                const todo = data;
                for (let i = 0; i < todo.length; i++) {
                    let subhtml = '<div>' +
                        '<input type="checkbox" id="todayTodo' + todo[i]["id"]+ '" value="' + todo[i]["id"]+ '">\n' +
                        '<label for="todayTodo' + todo[i]["id"]+ '"> ' + todo[i]["name"]+ '</label>' +
                        '<i class="fa-regular fa-square-minus fa-sm i today-minus" ></i> ' +
                        '<i class="fa-regular fa-star fa-sm i star"></i> </div>';
                    $("#todayList").append(subhtml);
                    todayTodoIds.push(todo[i].id+"");
                }
                addTodayTodosListener();
            }
        });
        $('input[type=checkbox]:checked').prop('disabled', true);
    });

    $(document).on("click", ".sub-cate", function() {  //서브카테고리 클릭시 할일 가져오기
        let subCateId = $(this).attr('data-bs-target').substring(9);
        if(getSubCategories[subCateId]==false){  //카테고리 내용 가져오지 않았었으면
            $.ajax({
                url: '/categories/sub/'+subCateId,
                type: 'GET',
                success: function(data) {
                    const todos = data;
                    for (let i = 0; i < todos.length; i++) {
                        let subhtml = '<ul class="list-unstyled sub-todo todo">';
                        if(todayTodoIds.includes(todos[i]["id"]+"")){
                            subhtml += '<input type="checkbox" id="todo'+todos[i]["id"]+ '" value="' + todos[i]["id"]+ '" disabled checked>\n';
                        }else{
                            subhtml += '<input type="checkbox" id="todo'+todos[i]["id"]+ '" value="' + todos[i]["id"]+ '" >\n';
                        }
                        subhtml += '<label for="todo' + todos[i]["id"]+ '"> ' + todos[i]["name"]+ '</label>'
                            + '<i class="fa-regular fa-trash-can fa-sm i del"></i>'
                            + '<i class="fa-regular fa-pen-to-square fa-sm i edit"></i> </ul>';
                        $("#sub-cate"+subCateId).append(subhtml);
                    }
                    getSubCategories[subCateId] = true;
                }
            })
        }
    });
    $(document).on("click", ".today-minus", function(){  //오늘할일에서 제외 클릭시
        var id = $(this).prev().prev().val();
        $.ajax({
            url: '/todos/today',
            type: 'DELETE',
            data: id,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            context: this,
            success: function (data) {
                if(data == id){
                    $(this).parent().remove();
                    $('input[value='+id+']').prop('disabled', false);
                    $('input[value='+id+']').prop('checked', false);
                    todayTodoIds.splice(todayTodoIds.indexOf(id), 1);
                }
            }
        });
    });
    function addTodayTodosListener(){
        let items = document.querySelectorAll('#todayList > div'); //Drag and Drop Sortable List
        items.forEach(item => {
            $(item).prop('draggable', true);
            item.addEventListener('dragstart', dragStart);
            item.addEventListener('drop', dropped);
            item.addEventListener('dragenter', cancelDefault);
            item.addEventListener('dragover', cancelDefault);
        })
    }
    function dragStart (e) {
        var index = $(e.target).index();  //클릭한 요소의 인덱스
        e.dataTransfer.setData('text/plain', index);
    }
    function dropped (e) {
        cancelDefault(e);
        // get new and old index
        let oldIndex = e.dataTransfer.getData('text/plain'); //드래그앤드롭으로 이동한 데이터
        // let target = $(e.target)  //이벤트가 발생한 요소(drop한 곳에 위치한 객체)
        let target;
        if ($(e.target).is('div')) {
            target = $(e.target);
        }else{
            target = $(e.target).parent();
        }
        let newIndex = target.index();

        // remove dropped items at old place
        let dropped = $(this).parent().children().eq(oldIndex).remove(); //oldIndex인 요소 삭제
        console.log("newIndex: "+newIndex+", oldIndex: "+oldIndex);

        if(newIndex == 0) {
            $('#todayList div:nth-child(1) i:last-child').remove(); //1순위 숫자1 제거
        }
        // insert the dropped items at new place
        if (newIndex < oldIndex) {
            target.before(dropped); //target 앞에 추가
        } else {
            target.after(dropped);
        }
        //3순위 border 컬러 갱신
        if(newIndex <= 2 || oldIndex <= 2){  //3순위 이하꺼를 옮기거나, 3순위 이하로 옮겨지면
            $('#today ul div').slice(0,3).css('border', '1px solid #f0a591');
            $('#today ul div').slice(3).css('border', '1px solid #ccc');
        }
        if(newIndex == 0 || oldIndex == 0) {  //0위치가 변경됬으면
            $('#today ul div:nth-child(1)').append('<i class="fa-solid fa-1 fa-sm p1"></i>');
        }
        if(oldIndex == 0){  //기존 위치가 0이면
            let todoNewIndex = newIndex + 1;
            $('#todayList div:nth-child('+todoNewIndex+') i:last-child').remove();
        }
    }
    function cancelDefault (e) {
        e.preventDefault();
        e.stopPropagation();
        return false;
    }
    $(document).on('click', 'i.edit', function() { //할일수정
        $(this).siblings('label').hide();
        if ($(this).siblings('input[type="text"]').length) {
            $(this).siblings('input[type="text"]').show();
            $(this).siblings('input:text').focus();
        } else {
            var text = $(this).siblings('label').text();
            $(this).before('<input type="text" value="'+text+'" />');
            $(this).siblings('input:text').focus();
            $(this).siblings('input:text').val('').val(text);
        }
    });
    $(document).on('blur', '.todo input:text', function(e) {  //할일수정 후
        $(this).siblings('label').text($(this).val()).show();
        $(this).hide();
        var newName = $(this).val();
        var id = $(this).siblings('input:checkbox').val();
        $.ajax({
            url: '/todos/'+id,
            type: 'PUT',
            data: JSON.stringify({name: newName}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(res) {
                if(id == res){
                    $("#info").fadeIn();
                    $("#info").fadeOut();
                    if(todayTodoIds.includes(id)){
                        $("#todayList > div > input:checkbox[value="+id+"]").next().text(newName);
                    }
                }
            }
        });
    });
    $(document).on('click', 'i.cate-edit', function() { //카테고리 수정
        let cateName = $(this).siblings('button').text();
        $(this).siblings('button').text('');
        if ($(this).siblings('input:text').length) {
            $(this).siblings('input:text').show();
            $(this).siblings('input:text').focus();
        } else {
            $(this).before('<input type="text" value="'+cateName+'" />');
            $(this).prev('input:text').focus();
            $(this).prev('input:text').val('').val(cateName);
        }
    });
    $(document).on('blur', '.cate input:text', function(e) { //카테고리 수정후
        let cateName = $(this).val();
        $(this).siblings('button').text(cateName);
        $(this).siblings('button').show();
        $(this).hide();
        let cateId = $(this).siblings('button').val();
        $.ajax({
            url: '/categories/'+cateId,
            type: 'PUT',
            data: JSON.stringify({name: cateName}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(res) {
                if(cateId == res){
                    $("#info").fadeIn();
                    $("#info").fadeOut();
                }
            }
        });
    });
    $(document).on('click','i.del',function (){ //할일 삭제
        var id = $(this).siblings('input:checkbox').val();
        var parent = $(this).parent();
        $.ajax({
            url: '/todos',
            type: 'DELETE',
            data: JSON.stringify({id: id}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(data) {
                if(data == id){
                    parent.remove();
                    if(todayTodoIds.includes(id)){
                        $("#today input[type='checkbox'][value="+id+"]").parent().remove();
                        todayTodoIds.splice(todayTodoIds.indexOf(id), 1);
                    }
                }
            }
        })
    });
    $(document).on('click','i.cate-del',function (){ //카테고리 삭제(하위카테고리도 삭제)
        var id = $(this).siblings('button').val();
        var parent = $(this).parent();
        $.ajax({
            url: '/categories/'+id,
            type: 'DELETE',
            context: this,
            success: function(data) {
                //상위/서브카테고리 삭제시 할일들 -> 노카테고리zone으로 보내기
                if(data == id){
                    if($(this).siblings('button').hasClass("sub-cate")){//서브카테고리 삭제시 하위할일 밑으로
                        let subTodos = $(this).parent().next('div').children();
                        subTodos.removeClass('sub-todo');
                        subTodos.appendTo('#noCateZone');
                        $(this).parent().next('div').children().remove();
                    }else{  //상위카테고리 삭제시에도 할일들 밑으로(노카테고리 영역으로)
                        let todos = $(this).parent().next('div').children('ul');
                        todos.appendTo('#noCateZone');
                        $(this).parent().next('div').children('ul').remove();
                    }
                    parent.remove();
                }
            }
        })
    });
    $(document).on('click','i.star',function (){
        var todoId = $(this).siblings('input:checkbox').val();
        if($(this).hasClass('star-on')) {
            $.ajax({
                url: '/today',
                type: 'PUT',
                data: JSON.stringify({todoId: todoId, important: false}),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                context: this,
                success: function(res) {
                    if(todoId == res){
                        $( this ).removeClass( "star-on fa-solid" );
                        $( this ).addClass( "fa-regular" );
                    }
                }
            });
        } else {
            $.ajax({
                url: '/today',
                type: 'PUT',
                data: JSON.stringify({todoId: todoId, important: true}),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                context: this,
                success: function(res) {
                    if(todoId == res){
                        $( this ).removeClass( "fa-regular" );
                        $( this ).addClass( "star-on fa-solid" );
                    }
                }
            });
        }
    });
});
