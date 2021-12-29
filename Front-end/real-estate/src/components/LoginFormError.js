import React from 'react';
import { Container, Form, Modal, Button } from 'react-bootstrap';

const LoginFormError = ({ show, onHide }) => {
    return (
        <Modal
            show={show}
            onHide={onHide}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
        >
            <Container>
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title-vcenter">Error Message</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group>
                            <Form.Label>Failed Login</Form.Label>
                        </Form.Group>

                        <Form.Group>
                            <Form.Label>Please check your email address or password</Form.Label>
                        </Form.Group>

                        <Button block variant="info" type="button" className="my-3" onClick={onHide}>
                            Close
                        </Button>
                    </Form>
                </Modal.Body>
            </Container>
        </Modal>
    );
};

export default LoginFormError;