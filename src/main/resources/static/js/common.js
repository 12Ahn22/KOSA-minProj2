const formToSerialize = (formId) => JSON.stringify([].reduce.call(document.querySelector('#' + formId), (data, element) => {
            //이름이 있는 것을 대상으로함
            if (element.name == '') return data;
            //radio와 checkbox인 경우는 반드시 선택된 것만 대상으로함
            if (element.type == 'radio' || element.type == 'checkbox') {
                if (element.checked) {
                    if (typeof data[element.name] == 'undefined') {
                        //문자열 1건 추가
                        if (document.querySelector("#" + formId).querySelectorAll("[name='" +element.name+ "']").length == 1 || element.type == 'radio') {
                            //문자열 값을 배열로 변경
                            data[element.name] = element.value;
                        } else if (element.type == 'checkbox') {
                            //배열로 변경
                            data[element.name] = [element.value];
                        }
                    } else if(typeof data[element.name] == 'string') {
                        //문자열 값을 배열로 변경
                        data[element.name] = [data[element.name], element.value];
                    } else if(typeof data[element.name] == 'object') {
                        //배열에 문자열 값을 추가
                        data[element.name].push(element.value);
                    }
                }
            } else {
                //그외는 모두 대상으로 함
                data[element.name] = element.value;
            }
            return data;
        },
        {} //초기값
    )
);