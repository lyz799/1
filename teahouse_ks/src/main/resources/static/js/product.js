const API_URL = 'http://localhost:8080/products'; // 后端API接口

// 添加产品
document.getElementById('addProductForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const product = {
        id: document.getElementById("id").value,
        name: document.getElementById('name').value,
        category: document.getElementById('category').value,
        price: parseFloat(document.getElementById('price').value),
        stock: parseInt(document.getElementById('stock').value),
    };

    fetch(`${API_URL}/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(product),
    })
        .then(response => {
            // 如果响应状态码不是 2xx，抛出错误
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(errorData.message || '发生未知错误');
                });
            }
            // 正常处理成功响应
            return response.json();
        })
        .then(data => {
            document.getElementById('addProductResult').innerText = '添加成功: ';
        })
        .catch(error => {
            // 捕获并显示错误信息
            document.getElementById('addProductResult').innerText = '添加失败: ' + error.message;
        });
});


// 查询产品
function getProduct() {
    const id = document.getElementById('getProductId').value;

    // 验证输入是否为空
    if (!id) {
        document.getElementById('getProductResult').innerText = '请输入产品ID';
        return;
    }

    fetch(`${API_URL}/getid/${id}`)
        .then(response => {
            if (response.ok) {
                // ID 存在时，返回产品信息
                return response.json();
            } else if (response.status === 404) {
                // ID 不存在时，返回 404 错误消息
                return response.json().then(errorData => {
                    throw new Error(errorData.message);
                });
            } else if (response.status === 204) {
                // 无内容时，显示没有找到产品
                throw new Error('没有找到产品');
            } else {
                throw new Error('服务器错误');
            }
        })
        .then(data => {
            // 格式化产品信息为中文描述
            const productInfo = `
                产品ID: ${data.id}
                名称: ${data.name}
                类别: ${data.category}
                价格: ${data.price} 元
                库存: ${data.stock} 件
                入库日期: ${new Date(data.entryDate).toLocaleString('zh-CN')}
            `;
            document.getElementById('getProductResult').innerText = productInfo;
        })
        .catch(error => {
            // 显示错误消息
            document.getElementById('getProductResult').innerText = '查询失败: ' + error.message;
        });
}


//更新产品
document.getElementById('updateProductForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const productId = document.getElementById('updateId').value;
    const updatedProduct = {
        name: document.getElementById('updateName').value,
        category: document.getElementById('updateCategory').value,
        price: document.getElementById('updatePrice').value ? parseFloat(document.getElementById('updatePrice').value) : null,
        stock: document.getElementById('updateStock').value ? parseInt(document.getElementById('updateStock').value) : null,
    };

    fetch(`${API_URL}/update/${productId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedProduct),
    })
        .then(async (response) => {
            const data = await response.json();
            const resultElement = document.getElementById('updateProductResult');
            resultElement.innerText = ''; // 清空之前的内容

            if (response.ok) {
                // 成功的响应，显示详细产品信息
                resultElement.innerText = `更新成功！\n产品信息：\n` +
                    `ID：${data.product.id}\n` +
                    `名称：${data.product.name}\n` +
                    `分类：${data.product.category}\n` +
                    `价格：${data.product.price}\n` +
                    `库存：${data.product.stock}\n` +
                    `更新时间：${new Date(data.product.entryDate).toLocaleString()}`;
            } else {
                // 错误的响应，根据 message 提示
                if (data.message === "ID不存在") {
                    resultElement.innerText = "更新失败：ID不存在";
                } else if (data.message === "请填写完整信息") {
                    resultElement.innerText = "更新失败：请填写完整信息";
                } else {
                    resultElement.innerText = `更新失败：${data.message}`;
                }
            }
        })
        .catch(error => {
            console.error("请求错误:", error);
            document.getElementById('updateProductResult').innerText = '更新失败：发生错误，请稍后重试';
        });
});


// 删除产品
function deleteProduct() {
    const id = document.getElementById('deleteProductId').value;
    fetch(`${API_URL}/delete/${id}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.status === 204) {
                document.getElementById('deleteProductResult').innerText = '删除成功';
            } else {
                document.getElementById('deleteProductResult').innerText = 'id不存在';
            }
        })
        .catch(error => {
            document.getElementById('deleteProductResult').innerText = '删除失败: ' + error.message;
        });
}

function getProductsInPriceRange() {
    const minPrice = parseFloat(document.getElementById('minPrice').value);
    const maxPrice = parseFloat(document.getElementById('maxPrice').value);

    fetch(`${API_URL}/price-range/${minPrice}/${maxPrice}`)
        .then(response => response.json())
        .then(data => {
            const resultElement = document.getElementById('priceRangeResult');
            resultElement.innerText = ''; // 清空之前的内容

            if (data.length > 0) {
                let resultText = '符合条件的产品:\n';
                data.forEach(product => {
                    resultText += `\n产品ID: ${product.id}\n` +
                        `名称: ${product.name}\n` +
                        `分类: ${product.category}\n` +
                        `价格: ¥${product.price.toFixed(2)}\n` +
                        `库存: ${product.stock}\n` +
                        `上架时间: ${new Date(product.entryDate).toLocaleString()}\n`;
                    resultText += '--------------------\n';
                });
                resultElement.innerText = resultText;
            } else {
                resultElement.innerText = '没有符合条件的产品。';
            }
        })
        .catch(error => {
            console.error("查询错误:", error);
            document.getElementById('priceRangeResult').innerText = '没有符合条件的产品';
        });
}


// 计算总价
function calculateTotalPrice() {
    const id = document.getElementById('calculateProductId').value;
    const quantity = document.getElementById('quantity').value;
    fetch(`${API_URL}/calculate-total/${id}/${quantity}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('totalPriceResult').innerText = '总价: ' + data;
        })
        .catch(error => {
            document.getElementById('totalPriceResult').innerText = '计算失败: ' +"id不存在";
        });
}
