let todayTodoIds = new Array();

function showSubCategories(e) {
    console.log(Object.keys(getSubCategories));
    console.log(Object.values(getSubCategories));
    var categoryId = $(e).val();
    if($(e).attr('aria-expanded')=="true" && getSubCategories[categoryId]==false){
        var url = "/categories/"+categoryId+"/subcategories";
        $.ajax({
            url: url,
            type:"GET",
            cache: false
        })
        .done(function (data, textStatus, jqXHR) {
            const subCateArr = data;
            for (let i = 0; i < subCateArr.length; i++) {
                let subhtml = '<button class="btn btn-toggle align-items-center rounded collapsed ms-4" data-bs-toggle="collapse" '
                    + 'data-bs-target="#sub-cate' + subCateArr[i]["id"]+ '"> ' + subCateArr[i]["name"] + '</button>'
                    + '<div class="collapse" id="sub-cate' + subCateArr[i]["id"]+ '"> </div>';
                $("#cate-collapse"+id).html(subhtml);
            }
            getSubCategories[categoryId] = true;
            getTodos(categoryId);
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
                let subhtml = '<ul class="btn-toggle-nav list-unstyled fw-normal pb-1 ps-1 ms-4">';
                if(todayTodoIds.includes(todos[i]["id"]+"")){  //숫자->문자
                    subhtml += '<input type="checkbox" id="todo' + todos[i]["id"]+ '" name="todo' + todos[i]["id"]+ '" value="' + todos[i]["id"]+ '" disabled checked>\n';
                }else{
                    subhtml += '<input type="checkbox" id="todo' + todos[i]["id"]+ '" name="todo' + todos[i]["id"]+ '" value="' + todos[i]["id"]+ '" >\n';
                }
                subhtml += '<label for="todo' + todos[i]["id"]+ '"> ' + todos[i]["name"]+ '</label>'
                    + '<i class="fa-regular fa-pen-to-square fa-sm edit"></i> </ul>';
                $("#cate-collapse"+categoryId).append(subhtml);
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
        // $('#todoArea #todo'+todoId).prop('disabled', true);
    });
    console.log(todayTodoIds);
}

$(document).ready(function() {

    getTodayTodoIds();
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
                        '<input type="checkbox" id="todayTodo' + todo[i]["id"]+ '" name="todayTodo' + todo[i]["id"]+ '" value="' + todo[i]["id"]+ '">\n' +
                        '<label for="todayTodo' + todo[i]["id"]+ '"> ' + todo[i]["name"]+ '</label>' +
                        '<i class="fa-regular fa-square-minus fa-sm today-minus" ></i> </div>';
                    $("#todayList").append(subhtml);
                }
                addTodayTodosListener();
            }
        });
        $('input[type=checkbox]:checked').prop('disabled', true);
    });
    $(document).on("click", ".today-minus", function(){  //i태그 - 버튼 클릭시
        var removeFromToday = $(this).prev().prev().val();
        console.log("삭제 클릭한 것: " + $(this).prev().text());
        $.ajax({
            url: '/todos/today',
            type: 'DELETE',
            data: removeFromToday,
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            context: this,
            success: function (data) {
                if(data == removeFromToday){
                    $(this).parent().remove();
                    $('input[value='+removeFromToday+']').prop('disabled', false);
                    $('input[value='+removeFromToday+']').prop('checked', false);
                }
            }
        });
    });
    function addTodayTodosListener(){
        // let items = document.querySelectorAll('#todayList > li') //Drag and Drop Sortable List
        let items = document.querySelectorAll('#todayList > div') //Drag and Drop Sortable List

        items.forEach(item => {
            $(item).prop('draggable', true)
            item.addEventListener('dragstart', dragStart)
            item.addEventListener('drop', dropped)
            item.addEventListener('dragenter', cancelDefault)
            item.addEventListener('dragover', cancelDefault)
        })
    }

    function dragStart (e) {
        var index = $(e.target).index()  //클릭한 요소의 인덱스
        e.dataTransfer.setData('text/plain', index)
    }

    function dropped (e) {
        cancelDefault(e)

        // get new and old index
        let oldIndex = e.dataTransfer.getData('text/plain') //드래그앤드롭으로 이동한 데이터
        // let target = $(e.target)  //이벤트가 발생한 요소(drop한 곳에 위치한 객체)
        let target;
        if ($(e.target).is('div')) {
            target = $(e.target);
        }else{
            target = $(e.target).parent();
        }
        console.log(target);
        let newIndex = target.index()

        // remove dropped items at old place
        let dropped = $(this).parent().children().eq(oldIndex).remove() //oldIndex인 요소 삭제
        console.log("newIndex: "+newIndex+", oldIndex: "+oldIndex)

        if(newIndex == 0) {
            $('#todayList div:nth-child(1) i:last-child').remove();
        }

        // insert the dropped items at new place
        if (newIndex < oldIndex) {
            target.before(dropped) //target 앞에 추가
        } else {
            target.after(dropped)
        }
        if(newIndex <= 2 || oldIndex <= 2){
            $('#today ul div').slice(0,3).css('border', '1px solid #f0a591');
            $('#today ul div').slice(3).css('border', '1px solid #ccc');
        }
        if(newIndex == 0 || oldIndex == 0) {
            $('#today ul div:nth-child(1)').append('<i class="fa-solid fa-1 fa-sm p1"></i>');
        }
        if(oldIndex == 0){
            let dragItemNewIndex = newIndex + 1;
            $('#todayList div:nth-child('+dragItemNewIndex+') i:last-child').remove();
        }
    }

    function cancelDefault (e) {
        e.preventDefault()
        e.stopPropagation()
        return false
    }

    $(document).on('click', 'i.edit', function() {
        $(this).siblings('label').hide();
        if ($(this).prev('input[type="text"]').length) {
            $(this).prev('input[type="text"]').show();
            $(this).prev('input').focus();
        } else {
            var text = $(this).prev('label').text();
            $(this).before('<input type="text" value="'+text+'" />');
            $(this).prev('input').focus();
            $(this).prev('input').val('').val(text);
        }
    });

    $(document).on('blur', 'input:text', function(e) {
        $(this).prev('label').text($(this).val());
        $(this).prev('label').show();
        $(this).hide();
        var text = $(this).val();
        var id = $(this).siblings('input:checkbox').val();
        $.ajax({
            url: '/todos/'+id,
            type: 'PUT',
            data: JSON.stringify({name: text}),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function(response) {
                console.log(response);
            }
        });
    });
});