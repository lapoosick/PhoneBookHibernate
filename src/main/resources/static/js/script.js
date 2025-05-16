class PhoneBookService {
    constructor() {
        this.baseUrl = "/api/contacts";
    }

    get(url, params) {
        return axios.get(url, {params}).then(response => response.data);
    }

    post(url, data) {
        return axios.post(url, data).then(response => response.data);
    }

    delete(url, data) {
        return axios.delete(url, data).then(response => response.data);
    }

    put(url, data) {
        return axios.put(url, data).then(response => response.data);
    }

    getContacts(term) {
        return this.get(this.baseUrl, {term});
    }

    createContact(contact) {
        return this.post(this.baseUrl, contact);
    }

    deleteContact(id) {
        return this.delete(`${this.baseUrl}/${id}`);
    }

    updateContact(contact) {
        return this.post(this.baseUrl, contact);
    }
}

Vue.createApp({
    data() {
        return {
            contacts: [],
            surname: "",
            name: "",
            phoneNumber: "",
            term: "",
            service: new PhoneBookService(),
            isSurnameInvalid: false,
            isNameInvalid: false,
            isPhoneNumberInvalid: false
        };
    },

    created() {
        this.getContacts();
    },

    methods: {
        getContacts() {
            this.term = "";
            this.findContacts();
        },

        findContacts() {
            this.service.getContacts(this.term)
                .then(contacts => {
                    this.contacts = contacts;
                })
                .catch(response => {
                    alert(response.message);
                });
        },

        createContact() {
            this.isSurnameInvalid = false;
            this.isNameInvalid = false;
            this.isPhoneNumberInvalid = false;

            if (this.surname.length === 0) {
                this.isSurnameInvalid = true;
                return;
            }

            if (this.name.length === 0) {
                this.isNameInvalid = true;
                return;
            }

            if (this.phoneNumber.length === 0) {
                this.isPhoneNumberInvalid = true;
                return;
            }

            const contact = {
                surname: this.surname,
                name: this.name,
                phoneNumber: this.phoneNumber,
                isEditing: false,
                editingSurname: this.surname,
                editingName: this.name,
                editingPhoneNumber: this.phoneNumber,
                isEditingSurnameInvalid: false,
                isEditingNameInvalid: false,
                isEditingPhoneNumberInvalid: false
            };

            this.service.createContact(contact)
                .then(response => {
                    if (!response.success) {
                        alert(response.message);
                    } else {
                        this.surname = "";
                        this.name = "";
                        this.phoneNumber = "";
                    }

                    this.getContacts();
                })
                .catch(response => {
                    alert(response.message);
                });
        },

        deleteContact(contact) {
            this.service.deleteContact(contact.id)
                .then(response => {
                    if (!response.success) {
                        alert(response.message);
                    }

                    this.getContacts();
                })
                .catch(response => {
                    alert(response.message);
                });
        },

        editContact(contact) {
            contact.editingSurname = contact.surname;
            contact.editingName = contact.name;
            contact.editingPhoneNumber = contact.phoneNumber;
            contact.isEditing = true;
        },

        saveContact(contact) {
            contact.isEditingSurnameInvalid = false;
            contact.isEditingNameInvalid = false;
            contact.isEditingPhoneNumberInvalid = false;

            if (contact.editingSurname.length === 0) {
                contact.isEditingSurnameInvalid = true;
                return;
            }

            if (contact.editingName.length === 0) {
                contact.isEditingNameInvalid = true;
                return;
            }

            if (contact.editingPhoneNumber.length === 0) {
                contact.isEditingPhoneNumberInvalid = true;
                return;
            }

            contact.surname = contact.editingSurname;
            contact.name = contact.editingName;
            contact.phoneNumber = contact.editingPhoneNumber;

            this.service.updateContact(contact)
                .then(response => {
                    if (!response.success) {
                        alert(response.message);
                    }

                    this.getContacts();
                })
                .catch(response => {
                    alert(response.message);
                });

            contact.isEditing = false;
        }
    }
}).mount("#app");