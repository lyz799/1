const API_BASE = '/api/rooms';

// 控制卡片展开和收起
const toggleCardContent = (cardId) => {
    const card = document.getElementById(cardId);
    const content = card.querySelector('.card-content');
    content.style.display = content.style.display === 'block' ? 'none' : 'block';
};

document.querySelectorAll('.expand-btn').forEach(button => {
    button.addEventListener('click', () => {
        const cardId = button.closest('.card').id;
        toggleCardContent(cardId);
    });
});

// 添加房间
document.getElementById('addRoomForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        roomId: +document.getElementById('addRoomId').value,
        name: document.getElementById('addRoomName').value,
        capacity: +document.getElementById('addRoomCapacity').value,
        available: document.getElementById('addRoomAvailable').checked,
        remainingUsageTime: +document.getElementById('addRoomRemainingUsageTime').value,
        updatedTime: document.getElementById('addRoomUpdatedTime').value,
    };

    const response = await fetch(`${API_BASE}/add`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });
    alert(await response.text());
});

// 更新房间
document.getElementById('updateRoomForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        roomId: +document.getElementById('updateRoomId').value,
        name: document.getElementById('updateRoomName').value,
        capacity: +document.getElementById('updateRoomCapacity').value,
        available: document.getElementById('updateRoomAvailable').checked,
        remainingUsageTime: +document.getElementById('updateRoomRemainingUsageTime').value,
        updatedTime: document.getElementById('updateRoomUpdatedTime').value,
    };

    const response = await fetch(`${API_BASE}/update`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    });
    alert(await response.text());
});

// 删除房间
document.getElementById('deleteRoomForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const roomId = +document.getElementById('deleteRoomId').value;
    const response = await fetch(`${API_BASE}/delete/${roomId}`, { method: 'DELETE' });
    alert(await response.text());
});

// 查询单个房间
document.getElementById('getRoomForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const roomId = +document.getElementById('getRoomId').value;
    const response = await fetch(`${API_BASE}/id/${roomId}`);

    if (response.ok) {
        const room = await response.json();
        if (room) {
            const roomInfo = `
                房间ID: ${room.roomId}<br>
                名称: ${room.name}<br>
                容量: ${room.capacity}<br>
                是否可用: ${room.available ? "是" : "否"}<br>
                剩余使用时间: ${room.remainingUsageTime} 小时<br>
                更新时间: ${new Date(room.updatedTime).toLocaleString('zh-CN')}
            `;
            document.getElementById('roomInfo').innerHTML = roomInfo;
        } else {
            document.getElementById('roomInfo').textContent = "房间ID不存在";
        }
    } else {
        document.getElementById('roomInfo').textContent = "房间ID不存在";
    }
});

// 查询剩余时间小于指定值的房间
document.getElementById('getRoomsByTimeForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const maxTime = +document.getElementById('remainingTime').value;

    const response = await fetch(`${API_BASE}/remainingUsageTimeLessThan/${maxTime}`);
    if (!response.ok) {
        document.getElementById('roomsByTimeInfo').textContent = `查询失败，状态码: ${response.status}`;
        return;
    }

    const rooms = await response.json();
    if (rooms.length > 0) {
        const roomInfo = rooms.map(room => `
            房间ID: ${room.roomId}<br>
            名称: ${room.name}<br>
            容量: ${room.capacity}<br>
            是否可用: ${room.available ? "是" : "否"}<br>
            剩余使用时间: ${room.remainingUsageTime} 小时<br>
            更新时间: ${new Date(room.updatedTime).toLocaleString('zh-CN')}
        `).join('<br><br>');
        document.getElementById('roomsByTimeInfo').innerHTML = roomInfo;
    } else {
        document.getElementById('roomsByTimeInfo').textContent = "没有找到符合条件的房间。";
    }
});

// 获取所有房间信息
document.getElementById('getAllRoomsButton').addEventListener('click', async () => {
    const response = await fetch(`${API_BASE}/all`);

    if (response.ok) {
        const rooms = await response.json();
        if (rooms.length > 0) {
            const roomInfo = rooms.map(room => `
                房间ID: ${room.roomId}<br>
                名称: ${room.name}<br>
                容量: ${room.capacity}<br>
                是否可用: ${room.available ? "是" : "否"}<br>
                剩余使用时间: ${room.remainingUsageTime} 小时<br>
                更新时间: ${new Date(room.updatedTime).toLocaleString('zh-CN')}
            `).join('<br><br>');
            document.getElementById('allRoomsInfo').innerHTML = roomInfo;
        } else {
            document.getElementById('allRoomsInfo').textContent = "没有可用的房间信息。";
        }
    } else {
        document.getElementById('allRoomsInfo').textContent = `查询失败，状态码: ${response.status}`;
    }
});
