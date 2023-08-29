<template>
  <b-container>
    <br>
    <h4>Log in</h4>
    <b-form @submit.stop.prevent @submit="onSubmit">
      <b-form-group label="Email:" label-for="username">
        <b-form-input :state="validatorUsername" id="username" v-model="form.username" type="text"
                      placeholder="Email" required></b-form-input>
        <b-form-invalid-feedback :state="validatorUsername">
          Email cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-group label="Password:" label-for="password">
        <b-form-input :state="validatorPassword" id="password" v-model="form.password" type="password"
                      placeholder="Password" required></b-form-input>
        <b-form-invalid-feedback :state="validatorPassword">
          Password cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-valid-feedback :state="validatorUsername && validatorPassword">

      </b-form-valid-feedback>
      <br>
      <b-button type="submit" variant="success">Log in</b-button>
    </b-form>
  </b-container>
</template>
<script>

export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      token: ''
    }
  },
  methods: {
    onSubmit(e) {
      e.preventDefault();
      const user = {
        email: this.form.username,
        password: this.form.password
      }
      console.log(user);
      if (this.validatorPassword && this.validatorUsername) {
        this.$axios.post('/api/users/login', {
          email: this.form.username,
          password: this.form.password
        }).then((response) => {
          localStorage.setItem('token', response.data.jwt);
          this.$parent.jwttoken = response.data.jwt;
          this.$router.push({name: 'News'});
          this.$root.jwttoken = response.data.jwt;
        }, () => {
          alert("INCORRECT CREDENTIALS OR YOUR STATUS IS INACTIVE!");
          this.form.password = '';
        })
      }
    }
  },
  computed: {
    validatorUsername() {
      return this.form.username.length >= 3 && this.form.username.length <= 256;
    },
    validatorPassword() {
      return this.form.password.length >= 1 && this.form.password.length <= 256;
    }
  }
}
</script>
<style scoped></style>