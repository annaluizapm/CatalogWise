const API_URL = "http://localhost:8080";

const produtoForm = document.getElementById("produtoForm");
const mensagem = document.getElementById("mensagem");
const produtosTabela = document.getElementById("produtosTabela");

async function carregarResumo() {
    const resposta = await fetch(`${API_URL}/relatorios/resumo`);
    const resumo = await resposta.json();

    document.getElementById("totalProdutos").textContent = resumo.totalProdutos;
    document.getElementById("produtosProntos").textContent = resumo.produtosProntos;
    document.getElementById("produtosRevisar").textContent = resumo.produtosRevisar;
    document.getElementById("produtosIncompletos").textContent = resumo.produtosIncompletos;
}

async function carregarProdutos() {
    const resposta = await fetch(`${API_URL}/produtos`);
    const produtos = await resposta.json();

    produtosTabela.innerHTML = "";

    produtos.forEach(produto => {
        const linha = document.createElement("tr");

        linha.innerHTML = `
            <td>${produto.sku}</td>
            <td>${produto.nome}</td>
            <td>${produto.marca}</td>
            <td>${produto.categoria}</td>
            <td>R$ ${produto.preco}</td>
            <td>${produto.estoque}</td>
            <td>${produto.pontuacao}</td>
            <td>${produto.status}</td>
        `;

        produtosTabela.appendChild(linha);
    });
}

produtoForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    const produto = {
        sku: document.getElementById("sku").value,
        nome: document.getElementById("nome").value,
        marca: document.getElementById("marca").value,
        categoria: document.getElementById("categoria").value,
        preco: parseFloat(document.getElementById("preco").value),
        estoque: parseInt(document.getElementById("estoque").value),
        descricao: document.getElementById("descricao").value
    };

    const resposta = await fetch(`${API_URL}/produtos`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(produto)
    });

    if (resposta.ok) {
        mensagem.textContent = "Produto cadastrado com sucesso!";
        mensagem.style.color = "green";

        produtoForm.reset();

        await carregarResumo();
        await carregarProdutos();
    } else {
        const erro = await resposta.json();
        mensagem.textContent = erro.mensagem || "Erro ao cadastrar produto.";
        mensagem.style.color = "red";
    }
});

carregarResumo();
carregarProdutos();