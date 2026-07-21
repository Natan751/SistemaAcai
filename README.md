# Sistema de Gerenciamento de Açaí 🍧

Este é um mini-sistema desenvolvido em Java para o gerenciamento de pedidos de uma loja de açaí. O sistema utiliza o padrão Façade para centralizar as operações e faz a persistência dos dados em um arquivo de texto (`produtos.txt`) através de serialização de objetos.

## Funcionalidades Principais
Cadastro de pedidos de clientes (com validação de ID).
Remoção de pedidos.
Pesquisa de produtos e clientes utilizando Java Streams.
Interface gráfica com barras de menu (Swing).
Persistência de dados (Salvar/Recuperar).

## Estrutura do Projeto
`src/`: Contém as classes principais do sistema e a Interface Gráfica.
`test/`: Contém a classe de testes automatizados (`SistemaAcaiTest`) usando JUnit.