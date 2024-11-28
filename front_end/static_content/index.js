
class FormValidator {
    form_x_input;
    form_y_input;
    form_r_input;

    checkXInput = () => {
        if (this.form_x_input.value === "") {
            this.form_x_input.setCustomValidity("Выберите значение.");
            return false;
        }

        this.form_x_input.setCustomValidity("");
        return true;
    };

    checkYInput = () => {
        if (this.form_y_input.value === "") {
            this.form_y_input.setCustomValidity(
                "Это поле не должно быть пустым."
            );
            return false;
        }

        let pattern = new RegExp(/^-?\d+([\.,]\d+)?$/, "g");

        if (!pattern.test(this.form_y_input.value)) {
            this.form_y_input.setCustomValidity("Введите число.");
            return false;
        }

        if (this.form_y_input.value > 3 || this.form_y_input.value < -3) {
            this.form_y_input.setCustomValidity(
                "Число должно быть в пределе [-3, 3]."
            );
            return false;
        }

        this.form_y_input.setCustomValidity("");
        return true;
    };

    checkRInput = () => {
        if (this.form_r_input.value === "") {
            this.form_r_input.setCustomValidity("Выберите значение.");
            return false;
        }

        if (!["1", "2", "3", "4", "5"].includes(this.form_r_input.value)) {
            this.form_r_input.value = "";
            this.form_r_input.setCustomValidity("Выберите НОРМАЛЬНОЕ значение.");
            return false;
        }

        this.form_r_input.setCustomValidity("");
        return true;
    };

    constructor() {
        this.form_x_input = $("#form-x-input")[0];
        this.form_y_input = $("#form-y-input")[0];
        this.form_r_input = $("#form-r-input")[0];

        $(".form-r-buttons").each((i, element) => {
            $(element).on("click", () => {
                $(this.form_r_input).val($(element).val());

                $(".form-r-buttons").each((i, removeClass) => {
                    $(removeClass).removeClass("r-button-selected");
                });

                $(element).addClass("r-button-selected");

                changeBoardR($(this.form_r_input).val());
            });
        });

        let checkRFunction = () => {this.checkRInput()}
        let checkXFunction = () => {this.checkXInput()}
        let checkYFunction = () => {this.checkYInput()}

        this.form_r_input.oninvalid = checkRFunction
        this.form_r_input.oninput = checkRFunction
        
        this.form_x_input.oninput = checkXFunction
        this.form_x_input.oninvalid = checkXFunction

        this.form_y_input.oninvalid = checkYFunction
        this.form_y_input.oninput = checkYFunction

        document.forms.main.onsubmit = onSubmit;
    }

    checkAllValid = () => {
        let isNotValid = [
            !this.checkRInput(),
            !this.checkYInput(),
            !this.checkXInput(),
        ].reduce((prev, curr) => prev || curr);
    
        return !isNotValid;
    }
}

const form_validator = new FormValidator();
const table_element = $("#table");

var onSubmit = async (event) => {
    event.preventDefault();

    if (!form_validator.checkAllValid()) {
        alert("чеее как ты сломал");
        return;
    }

    sendRequest(form_validator.form_x_input.value, form_validator.form_y_input.value, form_validator.form_r_input.value);
} 

var sendRequest = async (x,y,r) => {
    let request_body = JSON.stringify({ x: x, y: y, r: r });

    let response = await fetch("/fcgi-bin/lab1.jar", {
        method: "POST",
        body: request_body,
        headers: {
            accept: "application/json",
        },
    });

    let json = await response.json();

    if (json["error"] != undefined) {
        alert(json.error);
        return;
    }

    addPointFromData(json);
}

const addPointFromData = (json) => {
    let tableEntry = "<tr>";
    let data = [json.x, json.y, json.r, json.hit, json.duration_milliseconds, json.server_time];
    data.forEach((str) => {
        tableEntry += `<td>${str}</td>`
    });
    tableEntry += "</tr>";

    table_element.html(table_element.html() + tableEntry);

    createNewHitPoint(
        json.x,
        json.y,
        json.r,
        {
            size: pointRadius,
            name: json.server_time,
            color: json.hit ? "green" : "red",
        }
    )
}


async function loadHitHistory() {

    changeBoardR(1);

    let response = await fetch("/fcgi-bin/lab1.jar", {
        method: "GET",
        headers: {
            accept: "application/json",
        },
    });

    let json = await response.json();

    if (json["error"] != undefined) {
        alert(json.error);
        return;
    }
    
    let i = 0;
    while(json[i] != undefined) {
        addPointFromData(json[i]);
        i++;
    }
}

loadHitHistory();
