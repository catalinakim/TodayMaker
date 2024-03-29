let todayTodoIds = new Array();

function showSubCategories(e) {
    var id = $(e).val();
    if($(e).attr('aria-expanded')=="true" && getSubCategories[id]==false){
        var url = "/categories/"+id;
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
                $("#cate"+id).append(subhtml);
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
function hidePriorityBtn(){
    $('#today-each .pa1').hide();
    $('#today-each .pa2').hide();
    $('#today-each .pa3').hide();
}
function getPriIconTag(num) {
    var str = '<a class="btn pa' + num + '"> <i class="fa-solid fa-' + num + ' fa-xs"></i> </a>';
    return str;
}
$(document).ready(function() {
    getTodayTodoIds();
    todayTodoCheck();
    // hidePriorityBtn();
    todayImportantOn();
    addTodayTodosListener();

    $("#add").click(function() {  //오늘할일에 추가
        var checkedTodos = $('input[type=checkbox]:checked:not(:disabled)').map(function() {
            return this.value;
        }).get();
        $.ajax({
            url: '/today',
            type: 'POST',
            data: JSON.stringify(checkedTodos),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function (data) {
                const row = data;
                for (let i = 0; i < row.length; i++) {
                    let subhtml = '<div id="today-each" class="d-flex">' +
                        '<input type="hidden" id="dailyId" value="'+row[i].id+'"/>' +
                        '<input type="hidden" id="priority" value="'+row[i].priority+'"/>' +
                        '<input type="checkbox" id="todayTodo' + row[i].todoId+ '" value="' + row[i].todoId+ '">' +
                        '<label for="todayTodo' + row[i].todoId+ '">' + row[i].name+ '</label>' +
                        '<div id="pri-btn-div"></div>' +
                        '<div class="dropdown" id="pri-div">' +
                        '   <button class="btn btn-secondary " type="button" id="dropdownMenu" data-bs-toggle="dropdown" aria-expanded="false">\n' +
                        '       <i class="fas fa-bars fa-sm"></i>' +
                        '   </button>' +
                        '   <ul class="dropdown-menu">' +
                        '       <a class="dropdown-item" value=1>1</a>' +
                        '       <a class="dropdown-item" value=2>2</a>' +
                        '       <a class="dropdown-item" value=3>3</a>' +
                        '   </ul>' +
                        '</div>' +
                        '<i class="fa-regular fa-star fa-sm i star"></i>' +
                        '<i class="fa-regular fa-square-minus fa-sm i today-minus"></i> </div>';
                    $("#todayList").append(subhtml);
                    todayTodoIds.push(row[i].todoId+"");
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
        var id = $(this).siblings('input:checkbox').val();
        $.ajax({
            url: '/today',
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
    function addTodayTodosListener(){  //Drag & Drop
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
        let oldIndex = e.dataTransfer.getData('text/plain'); //옮기려는 요소의 원래 위치
        let target;
        if ($(e.target).is('div#today-each')) {
            target = $(e.target);
        }else{
            target = $(e.target).closest('div#today-each');
        }
        // console.log(e.target.tagName, e.target.id);
        // console.log("target:", target.prop('tagName'), target.prop('id'));

        let newIndex = target.index();
        console.log("newIndex: "+newIndex+", oldIndex: "+oldIndex);

        if(oldIndex == newIndex) return;

        let dropped = $(this).parent().children().eq(oldIndex).remove();

        //새 위치에 추가
        if (newIndex < oldIndex) {
            target.before(dropped); //target 앞에 추가
        } else {
            target.after(dropped);
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
    $(document).on('keyup', '.todo input:text', function(e) {
        if (e.keyCode === 13) {
            console.log("enter -> blur");
            $(this).blur();
        }
    });
    $(document).on('blur', '.todo input:text', function() {  //할일수정 후
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
                        $("#todayList input:checkbox[value="+id+"]").next('label').text(newName);
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
    $(document).on('keyup', '.cate input:text', function(e) {
        if (e.keyCode === 13) {
            console.log("enter -> blur");
            $(this).blur();
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
            url: '/todos/'+id,
            type: 'DELETE',
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
    $(document).on('click','i.cate-del',function (){ //카테고리 삭제(하위카테고리 후 삭제가능)
        var id = $(this).siblings('button').val();
        var parent = $(this).parent();
        //상위, 하위카테고리 여부
        var isSub = $(this).siblings('button').hasClass('sub-cate');  //true: 하위, false: 상위
        var TopHasNotSub = false;
        if(!isSub){  //상위카테고리이면 하위카테고리 여부 확인
            console.log("상위카테고리");
            $.ajax({
                url: '/categories/'+id+'/sub',
                type: 'GET',
                async: false,
                success: function(has) { //하위카테고리 존재시 alert
                    console.log("하위카테고리 존재여부", has);
                    if(has){
                        $(".cate-alert").fadeTo(2500, 500).slideUp(500, function(){
                            $(".cate-alert").slideUp(500);
                        });
                    }else{
                        TopHasNotSub = true;
                    }
                }
            })
        }
        //토글을 안열었었으면 할일 가져와서 보내기
        var hasTodo = $(this).parent().next().children('ul').hasClass('todo');
        console.log("hasTodo: ", hasTodo);
        if(hasTodo == false){
            if(!isSub){
                getTodos(id);
            }else{
                $(".sub-cate").trigger("click");
            }
        }
        if(isSub || TopHasNotSub){
            console.log("하위 or 하위없는 상위");
            $.ajax({
                url: '/categories/'+id,
                type: 'DELETE',
                // data: JSON.stringify({id: id, isSub: isSub}),
                // contentType: 'application/json; charset=utf-8',
                // dataType: 'json',
                context: this,
                success: function(data) {
                    //카테고리 삭제시 할일들 노카테고리존으로 보내기

                    if(data == id){
                        let todos = $(this).parent().next('div').children('ul');
                        if(isSub){
                            todos.removeClass('sub-todo');
                        }
                        todos.appendTo('#noCateZone');
                        parent.remove();
                    }
                }
            })
        }
    });
    $(document).on('click','i.star',function (){
        var dailyId = $(this).siblings('input:hidden[id="dailyId"]').val();
        $.ajax({
            url: '/today',
            type: 'PUT',
            data: JSON.stringify({id: dailyId, important: $(this).hasClass('star-on') ? false : true}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            context: this,
            success: function(res) {
                if(dailyId == res){
                    if($(this).hasClass('star-on')) {
                        $( this ).removeClass( "star-on fa-solid" );
                        $( this ).addClass( "fa-regular" );
                    }else{
                        $( this ).removeClass( "fa-regular" );
                        $( this ).addClass( "star-on fa-solid" );
                    }
                }
            }
        });
    });

    $(document).on('click', '.dropdown-menu a', function() {  //우선순위 지정
        var num = $(this).attr('value');
        var btnDiv = $(this).closest('#today-each').find('div#pri-btn-div');
        var clickTag = this;
        var AhadNum = $("ul#todayList").find("a.pa"+num);
        var pastId = AhadNum.parent().siblings('input:hidden[id="dailyId"]').val();
        if(pastId !== undefined){ //해당 순위가 다른 곳에 지정되어있었다면, 기존 것은 0으로 저장 후 remove
            $.ajax({
                url: '/today/priority',
                type: 'PUT',
                data: JSON.stringify({id: pastId, priority: 0}),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                success: function() {
                    AhadNum.remove();
                }
            });
        }
        var newId = $(clickTag).closest('#today-each').find('input:hidden[id="dailyId"]').val();
        console.log('new id:', newId);
        $.ajax({ //새로운 애는 우선순위 저장 후 html 대체
            url: '/today/priority',
            type: 'PUT',
            data: JSON.stringify({id: newId, priority: num}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function() {
                var iconTag = getPriIconTag(num);
                btnDiv.html(iconTag);  //해당 row 아이콘 넣기
            }
        });
    });
});
