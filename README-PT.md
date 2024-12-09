# Google Groups Manager - Guia de Utilização

## 📋 Pré-requisitos

- **Java** 23+
- **Spring Boot**
- **Conta Google Workspace** com uma **Organização** configurada
- **Conta de Serviço Google** configurada para acesso às APIs
- **Credenciais** na pasta `resources` do projeto

---

## 🔧 Configuração

### 1. Clonar o repositório
```bash
git clone https://github.com/diego2005eduardo/group-management-automation.git
cd group-management-automation
```
### 2. Configurar credenciais
1. **Criar credenciais no Google Cloud Console** (veja o tutorial detalhado na seção [🛠️ Tutorial de Configuração no Google](#google-setup-tutorial)).
2. Salvar o arquivo P12 das credenciais da **Conta de Serviço** na pasta `resources` do projeto com o nome `credentials`.
3. Configurar as credenciais no arquivo `application.properties`:
   ```properties
   googlegroup.service_account_id=
   googlegroup.service_account_user=user@domain.com
   googlegroup.filepath.p12=src/main/resources/credentials.p12
   ```
### 3. Executar o projeto
```bash
./gradlew bootRun
```

---

## 🌐 Endpoints Disponíveis

### 1. Adicionar Membro ao Grupo
**`POST /api/google-groups/{groupId}/members`**  
Adiciona um novo membro a um grupo específico.

**Request Body**:
```json
{
  "email": "usuario@exemplo.com",
  "role": "MEMBER"
}
```

**Response:**
```json
{
  "message": "Member successfully added."
}
```

---
### 2. Remover Membro de um Grupo
**`DELETE /api/google-groups/{groupId}/members/{email}`**  
Remove um membro de um grupo pelo e-mail.

**Response:**
```json
{
   "message": "Member successfully removed."
}
```
---
### 3. Listar Membros de um Grupo
**`GET /api/google-groups/{groupId}/members`**
Retorna todos os membros de um grupo.
```json
[
  {
    "email": "user1@example.com",
    "role": "MEMBER"
  },
  {
    "email": "user2@example.com",
    "role": "OWNER"
  }
]
```
---
<h2 id="google-setup-tutorial">🛠️ Tutorial de Configuração no Google</h2>

### Passo 1: Criar Projeto no Google Cloud Console
1. Acesse o [Google Cloud Console](https://console.cloud.google.com/).
2. Crie um novo projeto clicando em **Criar Projeto** no topo da página.

---

### Passo 2: Habilitar a Admin SDK API
1. No Google Cloud Console, vá para **APIs e Serviços > Biblioteca**.
2. Pesquise por **Admin SDK API** e clique em **Ativar**.

---

### Passo 3: Criar Credenciais
1. Acesse **APIs e Serviços > Credenciais** no menu lateral.
2. Clique em **Criar Credenciais** e escolha:
   - **Conta de Serviço**:
      - Crie uma conta de serviço para permitir acesso programático.
      - Após criar, baixe o arquivo P12 das credenciais e salve na pasta `resources`.
   - **Conta OAuth 2.0**:
      - Crie credenciais OAuth para autenticação adicional, caso necessário.

---

### Passo 4: Configurar Permissões no Google Admin
1. Acesse o **Google Admin Console** em [Admin Console](https://admin.google.com/).
2. Navegue para:  
   **Segurança > Controle de Dados e Acesso > Controles de API > Gerenciar Delegação**.
3. Adicione o **ID do Cliente** da **Conta de Serviço** e configure os seguintes escopos:  
   ```
   https://www.googleapis.com/auth/admin.directory.group
   https://www.googleapis.com/auth/admin.directory.group.member
   ```
4. Salve as alterações.

---

## 💻 Uso Básico com o Cliente

A seguir, um exemplo básico de uso da aplicação para adicionar membros a grupos:

### Exemplo de Adição de Membro ao Grupo
```java
// Exemplo de adição de membro
GoogleGroupsService service = new GoogleGroupsService();
Member member = service.createMember("usuario@exemplo.com", "MEMBER");
service.addMemberToGroup("grupo-exemplo", member);