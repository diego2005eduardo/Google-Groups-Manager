# Google Groups Manager - User Guide

## 📋 Prerequisites

- **Java** 23+
- **Spring Boot**
- **Google Workspace Account** with a configured **Organization**
- **Google Service Account** configured for API access
- **Credentials** in the `resources` folder of the project

---

## 🔧 Setup

### 1. Clone the Repository
```bash
git clone https://github.com/diego2005eduardo/group-management-automation.git
cd group-management-automation
```
### 2. Configure Credentials
1. **Create credentials in the Google Cloud Console** (see the detailed tutorial in the [🛠️ Google Setup Tutorial](#google-setup-tutorial) section).
2. Save the P12 file of the **Service Account** credentials in the `resources` folder of the project, naming it `credentials`.
3. Configure the credentials in the `application.properties` file:
   ```properties
   googlegroup.service_account_id=
   googlegroup.service_account_user=user@domain.com
   googlegroup.filepath.p12=src/main/resources/credentials.p12
   ```
### 3. Run the project
```bash
./gradlew bootRun
```

---

## 🌐 Available Endpoints

### 1. Add Member to Group
**`POST /api/google-groups/{groupId}/members`**  
Adds a new member to a specific group.

**Request Body**:
```json
{
  "email": "user@example.com",
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
### 2. Remove Member from Group
**`DELETE /api/google-groups/{groupId}/members/{email}`**  
Removes a member from a group by email.

**Response:**
```json
{
   "message": "Member successfully removed."
}
```
---
### 3. List Members of a Group
**`GET /api/google-groups/{groupId}/members`**
Returns all members of a group.
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
<h2 id="google-setup-tutorial">🛠️ Google Setup Tutorial</h2>

### Step 1: Create a Project in Google Cloud Console
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project by clicking on **Create Project** at the top of the page.

---

### Step 2: Enable Admin SDK API
1. In the Google Cloud Console, navigate to **APIs & Services > Library**.
2. Search for **Admin SDK API** and click **Enable**.

---

### Step 3: Create Credentials
1. Go to **APIs & Services > Credentials** in the sidebar menu.
2. Click **Create Credentials** and choose:
    - **Service Account**:
        - Create a service account to allow programmatic access.
        - After creating, download the P12 credential file and save it in the `resources` folder.
    - **OAuth 2.0 Account**:
        - Create OAuth credentials for additional authentication if needed.

---

### Step 4: Configure Permissions in Google Admin
1. Access the **Google Admin Console** at [Admin Console](https://admin.google.com/).
2. Navigate to:
   **Security > Data & Access Control > API Controls > Manage Delegation.**
3. Add the **Client ID** of the **Service Account** and configure the following scopes:
   ```
   https://www.googleapis.com/auth/admin.directory.group
   https://www.googleapis.com/auth/admin.directory.group.member
   ```
4. Save the changes.

---

## 💻 Basic Usage with the Client

Here is a basic example of how to use the application to add members to groups:

### Example of Adding a Member to a Group
```java
// Example of adding a member
GoogleGroupsService service = new GoogleGroupsService();
Member member = service.createMember("user@example.com", "MEMBER");
service.addMemberToGroup("example-group", member);