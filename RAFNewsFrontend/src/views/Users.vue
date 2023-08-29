<template>
  <div class="users">
    <br>
    <h1>All users</h1><br>
    <button type="button" class="btn btn-primary" @click="addUser"> Add a new user</button>
    <hr>
    <ul id="itemList">
      <li v-for="user in userList" :key="user.id">
        <div class="userInfoDiv">
          <br>
          <b><h4><u>USER: {{ user.first_name }} {{ user.last_name }} </u></h4></b>
          <hr>
          <div class="userInfoDiv" style="text-align: justify;">
            <b>
              <p>ID: {{ user.id }}</p>
              <p :id="user.id">STATUS: {{ user.status }}</p>
              <p>USER TYPE: {{ user.type }}</p>
            </b>
          </div>

          <div class="btn-group" role="group" aria-label="Basic example">
            <button v-if="isCreator(user.type)" type="button" class="btn btn-outline-primary"
                    @click="changeStatus(user.id, user.status)">Change status
            </button>
            <button type="button" class="btn btn-secondary" @click="editUser(user.id)">Edit</button>
            <button type="button" class="btn btn-danger" @click="deleteUser(user.id)">Delete</button>
          </div>
        </div>
      </li>
    </ul>

    <b-pagination
        v-model="currentPage"
        :total-rows="totalRows"
        :per-page="perPage"
        aria-controls="itemList"
        align="center"
        @input="onPageChange"
        :limit="5"
    >
    </b-pagination>

  </div>
</template>
<script>
export default {
  data() {
    return {
      userList: [],
      currentPage: 1,
      perPage: 2,
      items: [],
      totalRows: 0,
    }
  },
  computed: {
    rows() {
      return this.totalRows;
    }
  },
  mounted() {
    this.fetchUserCount();
    this.fetchUsers();

  },
  methods: {
    fetchUserCount() {
      this.$axios
          .get('/api/users/count')
          .then((response) => {
            const totalCount = response.data;
            this.totalRows = totalCount;
          })
          .catch((error) => {
            console.error('Error fetching user count:', error);
          });
    },
    fetchUsers() {
      this.$axios
          .get(`/api/users/allUsers?page=${this.currentPage}&perPage=${this.perPage}`)
          .then((response) => {
            this.userList = response.data;
          })
          .catch((error) => {
            console.error('Error fetching news:', error);
          });
    },
    onPageChange(newPage) {
      this.currentPage = newPage;
      this.fetchUsers();
    },
    isCreator(type) {
      if (type !== "ADMIN") {
        return true;
      }
    },
    changeStatus(id, status) {
      if (status.toString() === 'Active') {
        status = 'Non Active';
      } else {
        status = 'Active';
      }
      let email = '';
      for (const u of this.userList) {
        if (id === u.id) {
          email = u.email;
        }
      }
      if (email.toString() !== '') {
        this.$axios.get('/api/users/status/' + email).then((() => {
          window.location.reload();
        }));
      }
    },
    editUser(id) {
      this.$router.push('/edituser/' + id);
    },
    addUser() {
      this.$router.push({name: 'AddUser'});
    },
    deleteUser(id) {
      let email = '';
      for (const u of this.userList) {
        if (id === u.id) {
          email = u.email;
        }
      }
      if (email !== '') {
        this.$axios.delete('/api/users/delete/' + email).then((() => {
          window.location.reload();
        }));
      }
    }
  }
}

</script>
<style scoped>
.userInfoDiv {
  margin-top: 8px;
  margin-bottom: 12px;
  border: 2px solid black;
  width: 100%;
  padding-left: 24px;
  padding-right: 24px;
  padding-bottom: 12px;
  margin-bottom: 8px;
}

.users {
  text-align: center;
}

ul {
  list-style-type: none;
}


.btn {
  margin: 12px;
}
</style>