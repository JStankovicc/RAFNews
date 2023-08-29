<template>
  <b-container>
    <br>
    <h4>Edit user</h4>
    <b-form @submit.stop.prevent @submit="onSubmit">
      <b-form-group label="Email:" label-for="email">
        <b-form-input :state="validatorEmail" id="email" v-model="form.email" type="email" placeholder="Email"
                      required></b-form-input>
        <b-form-invalid-feedback :state="validatorEmail">
          Email cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-group label="First Name:" label-for="firstname">
        <b-form-input :state="validatorFN" id="firstname" v-model="form.firstname" type="text"
                      placeholder="First Name" required></b-form-input>
        <b-form-invalid-feedback :state="validatorFN">
          First name cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-group label="Last Name:" label-for="lastname">
        <b-form-input :state="validatorLN" id="lastname" v-model="form.lastname" type="text" placeholder="Last Name"
                      required></b-form-input>
        <b-form-invalid-feedback :state="validatorLN">
          Last name cannot be empty.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-group label="User type:" v-slot="{ ariaDescribedby }">
        <b-form-radio v-model="form.selected" :aria-describedby="ariaDescribedby" name="some-radios" value="A"
                      :state="validatorSelected">Admin
        </b-form-radio>
        <b-form-radio v-model="form.selected" :aria-describedby="ariaDescribedby" name="some-radios" value="B"
                      :state="validatorSelected">Content Creator
        </b-form-radio>
        <b-form-invalid-feedback :state="validatorSelected">
          User type must be selected.
        </b-form-invalid-feedback>
      </b-form-group>
      <b-form-valid-feedback
          :state="validatorEmail && validatorPassword && validatorFN && validatorLN && validatorSelected">

      </b-form-valid-feedback>
      <br>
      <b-button type="submit" variant="success">Edit User</b-button>
    </b-form>
  </b-container>
</template>
<script>

export default {
  data() {
    return {
      form: {
        email: '',
        firstname: '',
        lastname: '',
        password: '',
        selected: ''
      },
      oldemail: ''
    }
  },
  methods: {
    onSubmit(e) {
      e.preventDefault();
      console.log(this.form);
      if (this.validatorEmail && this.validatorFN && this.validatorLN && this.validatorPassword && this.validatorSelected) {
        let utype = 1;
        let ustatus = 0;
        if (this.form.selected == 'A') {
          utype = 0;
          ustatus = 1;
        }
        this.$axios.post('/api/users/update/' + this.oldemail, {
          email: this.form.email,
          first_name: this.form.firstname,
          last_name: this.form.lastname,
          type: utype
        }).then((response => {
          const user = response.data;
          if (user.id != null) {
            this.$router.push({name: 'Users'});
          } else {
            return;
          }
        }));
      } else {
        alert("All fields are required!");
        return;
      }

    }
  },
  computed: {
    validatorPassword() {
      return this.form.password.length >= 1 && this.form.password.length <= 256;
    },
    validatorEmail() {
      return this.form.email.length >= 3 && this.form.email.length <= 256;
    },
    validatorFN() {
      return this.form.firstname != '';
    },
    validatorLN() {
      return this.form.lastname != '';
    },
    validatorSelected() {
      return this.form.selected != '';
    }
  },
  mounted() {
    let id = this.$route.params.id;
    this.$axios.get('/api/users/id/' + id).then((response => {
      const user = response.data;
      this.form.email = user.email;
      this.oldemail = user.email;
      this.form.firstname = user.first_name;
      this.form.lastname = user.last_name;
      this.form.password = user.password;
      if (user.type == 1) {
        this.form.selected = "A";
      } else {
        this.form.selected = "B";
      }
    }));
  }
}

</script>
<style scoped></style>