function addMember() {
    const member = {
        id: document.getElementById("addId").value,
        name: document.getElementById("addName").value,
        phone: document.getElementById("addPhone").value,
        point: document.getElementById("addPoint").value,
        level: document.getElementById("addLevel").value
    };
    fetch("/members/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(member)
    })
        .then(response => response.text())
        .then(data => showAlert('success', data))
        .catch(error => showAlert('error', "发生错误: " + error));
}

function updateMember() {
    const member = {
        id: document.getElementById("updateId").value,
        name: document.getElementById("updateName").value,
        phone: document.getElementById("updatePhone").value,
        point: document.getElementById("updatePoint").value,
        level: document.getElementById("updateLevel").value
    };
    fetch("/members/update", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(member)
    })
        .then(response => {
            if (!response.ok) throw new Error("更新失败");
            return response.text();
        })
        .then(data => showAlert('success', data))
        .catch(error => showAlert('error', error.message));
}

function deleteMember() {
    const id = document.getElementById("deleteId").value;
    fetch("/members/delete/" + id, { method: "DELETE" })
        .then(response => {
            if (!response.ok) throw new Error("删除失败");
            return response.text();
        })
        .then(data => showAlert('success', data))
        .catch(error => showAlert('error', error.message));
}

function getMember() {
    const id = document.getElementById("getId").value;
    const phone = document.getElementById("getPhone").value;

    const url = id ? `/members/${id}` : phone ? `/members/phone/${phone}` : null;
    if (!url) {
        showAlert('error', "请输入ID或手机号!");
        return;
    }

    fetch(url)
        .then(response => {
            if (!response.ok) {
                // 处理ID不存在的情况
                return response.json().then(data => {
                    throw new Error(data.message || "无此成员");
                });
            }
            return response.json();
        })
        .then(data => {
            displayMemberInfo(data);
            showAlert('success', "查询成功!");
        })
        .catch(error => showAlert('error', error.message));
}


function displayMemberInfo(data) {
    document.getElementById("getName").textContent = "姓名: " + data.name;
    document.getElementById("getPhone").textContent = "电话: " + data.phone;
    document.getElementById("getPoint").textContent = "余额: " + data.point;
    document.getElementById("getLevel").textContent = "等级: " + data.level;
    document.querySelector(".info").style.display = "block";
}

function showAlert(type, message) {
    const alertBox = document.querySelector(".alert");
    alertBox.textContent = message;
    alertBox.classList.remove('success', 'error');
    alertBox.classList.add(type);
    alertBox.style.display = 'block';
    setTimeout(() => alertBox.style.display = 'none', 3000);
}

function toggleCollapse(id) {
    const collapse = document.getElementById(id);
    collapse.classList.toggle("show");
    if (id === 'allMembers') getAllMembers();
}

function getAllMembers() {
    fetch("/members/all")
        .then(response => response.json())
        .then(data => {
            const membersList = document.getElementById("membersList");
            membersList.innerHTML = "";
            data.forEach(member => {
                const li = document.createElement("li");
                li.textContent = `ID: ${member.id}, 姓名: ${member.name}, 电话: ${member.phone}, 余额: ${member.point}, 等级: ${member.level}`;
                membersList.appendChild(li);
            });
        })
        .catch(error => showAlert('error', "发生错误: " + error));
}
