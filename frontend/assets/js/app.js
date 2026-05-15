const API_URL = 'http://localhost:8080';

let produtosCache = [];
let statusFiltroAtual = 'TODOS';

function showToast(message) {
  const toast = document.getElementById('toast');

  if (!toast) {
    alert(message);
    return;
  }

  toast.textContent = message;
  toast.classList.add('show');

  setTimeout(() => {
    toast.classList.remove('show');
  }, 2600);
}

function formatarPreco(valor) {
  return Number(valor || 0).toLocaleString('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  });
}

function statusClasse(status) {
  if (status === 'PRONTO') return 'ok';
  if (status === 'REVISAR') return 'warn';
  return 'bad';
}

function statusTexto(status) {
  if (status === 'PRONTO') return 'Pronto';
  if (status === 'REVISAR') return 'Revisar';
  return 'Incompleto';
}

function usuarioEstaLogado() {
  return localStorage.getItem('usuarioLogado') === 'true';
}

function salvarUsuarioLogado(usuario) {
  localStorage.setItem('usuarioLogado', 'true');
  localStorage.setItem('usuarioId', usuario.id);
  localStorage.setItem('usuarioNome', usuario.nome);
  localStorage.setItem('usuarioEmail', usuario.email);
}

function sair() {
  localStorage.removeItem('usuarioLogado');
  localStorage.removeItem('usuarioId');
  localStorage.removeItem('usuarioNome');
  localStorage.removeItem('usuarioEmail');

  window.location.href = 'login.html';
}

function protegerPaginaPrincipal() {
  const estaNaPrincipal = document.getElementById('produtoForm') !== null;

  if (estaNaPrincipal && !usuarioEstaLogado()) {
    window.location.href = 'login.html';
  }
}

function configurarCadastro() {
  const cadastroForm = document.getElementById('cadastroForm');

  if (!cadastroForm) return;

  cadastroForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    const usuario = {
      nome: document.getElementById('name').value.trim(),
      email: document.getElementById('signup-email').value.trim(),
      senha: document.getElementById('signup-password').value
    };

    try {
      const resposta = await fetch(`${API_URL}/auth/cadastro`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuario)
      });

      if (resposta.ok) {
        showToast('Cadastro criado com sucesso! Faça login para continuar.');

        setTimeout(() => {
          window.location.href = 'login.html';
        }, 900);

        return;
      }

      const erro = await resposta.json().catch(() => null);
      showToast(erro?.mensagem || 'Erro ao cadastrar usuário.');
    } catch (error) {
      console.error(error);
      showToast('Não foi possível conectar com o servidor. Confira se o Spring está rodando.');
    }
  });
}

function configurarLogin() {
  const loginForm = document.getElementById('loginForm');

  if (!loginForm) return;

  loginForm.addEventListener('submit', async function (event) {
    event.preventDefault();

    const login = {
      email: document.getElementById('login-email').value.trim(),
      senha: document.getElementById('login-password').value
    };

    try {
      const resposta = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(login)
      });

      if (resposta.ok) {
        const usuario = await resposta.json();

        salvarUsuarioLogado(usuario);
        showToast('Login realizado com sucesso!');

        setTimeout(() => {
          window.location.href = 'principal.html';
        }, 700);

        return;
      }

      const erro = await resposta.json().catch(() => null);
      showToast(erro?.mensagem || 'Email ou senha inválidos.');
    } catch (error) {
      console.error(error);
      showToast('Não foi possível conectar com o servidor. Confira se o Spring está rodando.');
    }
  });
}

function configurarBotaoSair() {
  const btnSair = document.getElementById('btnSair');

  if (!btnSair) return;

  btnSair.addEventListener('click', sair);
}

function exibirUsuarioLogado() {
  const nomeUsuario = document.getElementById('usuarioNome');

  if (!nomeUsuario) return;

  nomeUsuario.textContent = localStorage.getItem('usuarioNome') || 'Usuário';
}

function filtrarProdutosPorBuscaEStatus() {
  const input = document.getElementById('productSearch');
  const termo = input ? input.value.toLowerCase().trim() : '';

  return produtosCache.filter(produto => {
    const combinaStatus = statusFiltroAtual === 'TODOS' || produto.status === statusFiltroAtual;
    const textoBusca = `${produto.nome} ${produto.sku} ${produto.marca} ${produto.categoria}`.toLowerCase();
    const combinaBusca = textoBusca.includes(termo);

    return combinaStatus && combinaBusca;
  });
}

function renderizarProdutos() {
  const tabela = document.getElementById('productTable');

  if (!tabela) return;

  const produtos = filtrarProdutosPorBuscaEStatus();
  tabela.innerHTML = '';

  if (produtos.length === 0) {
    tabela.innerHTML = '<tr><td colspan="6">Nenhum produto encontrado.</td></tr>';
    return;
  }

  produtos.forEach(produto => {
    const linha = document.createElement('tr');

    linha.dataset.name = `${produto.nome} ${produto.sku} ${produto.marca} ${produto.categoria}`;

    linha.innerHTML = `
      <td>
        <div class="product-cell">
          <span class="thumb">▣</span>
          <div>
            <strong>${produto.nome}</strong>
            <small>SKU: ${produto.sku} • ${produto.marca}</small>
          </div>
        </div>
      </td>
      <td>${produto.categoria}</td>
      <td>${formatarPreco(produto.preco)}</td>
      <td>${produto.pontuacao ?? 0}</td>
      <td><span class="status ${statusClasse(produto.status)}">${statusTexto(produto.status)}</span></td>
      <td>
        <div class="action-group">
          <button class="action-btn edit" type="button" onclick="prepararEdicao(${produto.id})">Editar</button>
          <button class="action-btn danger" type="button" onclick="excluirProduto(${produto.id})">Excluir</button>
        </div>
      </td>
    `;

    tabela.appendChild(linha);
  });
}

function filterProducts() {
  renderizarProdutos();
}

async function carregarResumo() {
  const total = document.getElementById('totalProdutos');

  if (!total) return;

  const resposta = await fetch(`${API_URL}/relatorios/resumo`);

  if (!resposta.ok) {
    throw new Error('Não foi possível carregar o resumo.');
  }

  const resumo = await resposta.json();

  document.getElementById('totalProdutos').textContent = resumo.totalProdutos;
  document.getElementById('produtosProntos').textContent = resumo.produtosProntos;
  document.getElementById('produtosRevisar').textContent = resumo.produtosRevisar;
  document.getElementById('produtosIncompletos').textContent = resumo.produtosIncompletos;

  const insightPrincipal = document.getElementById('insightPrincipal');
  const insightTexto = document.getElementById('insightTexto');

  if (insightPrincipal && insightTexto) {
    const pendentes = resumo.produtosRevisar + resumo.produtosIncompletos;

    insightPrincipal.textContent = `${pendentes} produto(s) precisam de atenção`;
    insightTexto.textContent = resumo.totalProdutos === 0
        ? 'Cadastre seu primeiro produto para visualizar a saúde do catálogo.'
        : 'Complete informações ausentes e melhore descrições para aumentar a pontuação média.';
  }
}

async function carregarProdutos() {
  const tabela = document.getElementById('productTable');

  if (!tabela) return;

  const resposta = await fetch(`${API_URL}/produtos`);

  if (!resposta.ok) {
    throw new Error('Não foi possível carregar os produtos.');
  }

  produtosCache = await resposta.json();
  renderizarProdutos();
}

async function atualizarTela() {
  await carregarResumo();
  await carregarProdutos();
}

function limparFormulario() {
  const form = document.getElementById('produtoForm');

  if (!form) return;

  form.reset();

  document.getElementById('produtoId').value = '';
  document.getElementById('formTitle').textContent = 'Cadastrar produto';
  document.getElementById('submitProduto').textContent = 'Salvar produto';
}

function abrirFormulario() {
  const panel = document.getElementById('productFormPanel');

  if (panel) {
    panel.classList.remove('hidden');
  }
}

function fecharFormulario() {
  const panel = document.getElementById('productFormPanel');

  if (panel) {
    panel.classList.add('hidden');
  }

  limparFormulario();
}

function prepararEdicao(id) {
  const produto = produtosCache.find(item => item.id === id);

  if (!produto) {
    showToast('Produto não encontrado para edição.');
    return;
  }

  abrirFormulario();

  document.getElementById('produtoId').value = produto.id;
  document.getElementById('sku').value = produto.sku;
  document.getElementById('nome').value = produto.nome;
  document.getElementById('marca').value = produto.marca;
  document.getElementById('categoria').value = produto.categoria;
  document.getElementById('preco').value = produto.preco;
  document.getElementById('estoque').value = produto.estoque;
  document.getElementById('descricao').value = produto.descricao;
  document.getElementById('formTitle').textContent = 'Editar produto';
  document.getElementById('submitProduto').textContent = 'Atualizar produto';
  document.getElementById('productFormPanel').scrollIntoView({ behavior: 'smooth' });
}

async function excluirProduto(id) {
  const confirmar = confirm('Tem certeza que deseja excluir este produto?');

  if (!confirmar) return;

  const resposta = await fetch(`${API_URL}/produtos/${id}`, {
    method: 'DELETE'
  });

  if (resposta.ok) {
    showToast('Produto excluído com sucesso!');
    await atualizarTela();
  } else {
    showToast('Erro ao excluir produto.');
  }
}

async function salvarProduto(event) {
  event.preventDefault();

  const id = document.getElementById('produtoId').value;

  const produto = {
    sku: document.getElementById('sku').value.trim(),
    nome: document.getElementById('nome').value.trim(),
    marca: document.getElementById('marca').value.trim(),
    categoria: document.getElementById('categoria').value.trim(),
    preco: parseFloat(document.getElementById('preco').value),
    estoque: parseInt(document.getElementById('estoque').value),
    descricao: document.getElementById('descricao').value.trim()
  };

  const url = id ? `${API_URL}/produtos/${id}` : `${API_URL}/produtos`;
  const metodo = id ? 'PUT' : 'POST';

  const resposta = await fetch(url, {
    method: metodo,
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(produto)
  });

  if (resposta.ok) {
    showToast(id ? 'Produto atualizado com sucesso!' : 'Produto cadastrado com sucesso!');
    fecharFormulario();
    await atualizarTela();
  } else {
    const erro = await resposta.json().catch(() => null);
    showToast(erro?.mensagem || 'Erro ao salvar produto.');
  }
}

function configurarPaginaPrincipal() {
  const form = document.getElementById('produtoForm');

  if (!form) return;

  protegerPaginaPrincipal();
  exibirUsuarioLogado();

  document.getElementById('toggleProductForm')?.addEventListener('click', () => {
    limparFormulario();
    abrirFormulario();
  });

  document.getElementById('cancelEdit')?.addEventListener('click', fecharFormulario);

  form.addEventListener('submit', salvarProduto);

  document.querySelectorAll('.filter-btn').forEach(botao => {
    botao.addEventListener('click', () => {
      document.querySelectorAll('.filter-btn').forEach(item => item.classList.remove('active'));
      botao.classList.add('active');
      statusFiltroAtual = botao.dataset.status;
      renderizarProdutos();
    });
  });

  atualizarTela().catch(error => {
    console.error(error);
    showToast('Não foi possível conectar com a API. Confira se o Spring está rodando.');
  });
}

document.addEventListener('DOMContentLoaded', function () {
  protegerPaginaPrincipal();
  configurarCadastro();
  configurarLogin();
  configurarBotaoSair();
  configurarPaginaPrincipal();
});