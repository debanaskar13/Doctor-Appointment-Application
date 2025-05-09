import React, { useContext, useEffect, useState } from 'react'
import { AdminContext } from '../../context/AdminContext'
import LoadWrapper from '../../components/Loading'
import ApiService from '../../api/Axios'
import { toast } from 'react-toastify'
import Pagination from '../../components/Pagination'

const UserList = () => {

    const { token } = useContext(AdminContext)
    const [loading, setLoading] = useState(false)
    const [users, setUsers] = useState([])
    const [editingUser, setEditingUser] = useState(null);
    const [formData, setFormData] = useState({ name: '', email: '', dob: '', gender: '', phone: '', image: '' });
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [totalPage, setTotalPage] = useState(0)
    const [page, setPage] = useState(1)
    const [sortField, setSortField] = useState('name')
    const [sortDirection, setSortDirection] = useState('ASC')
    const maxPagesToShow = 10
    const downArrow = "↓"
    const upArrow = "↑"

    const getUsers = async () => {
        setLoading(true)
        try {
            const { data } = await ApiService.fetchAllUsers(page, maxPagesToShow, sortField, sortDirection)
            // console.log(data)
            setUsers(data.users.data)
            setTotalPage(data.users.totalPages)

        } catch (error) {
            toast.error('something went wrong')

        } finally {
            setLoading(false)
        }
    }

    const handleEdit = (user) => {
        setEditingUser(user);
        setFormData({ ...user });
        setIsModalOpen(true);
    }

    const handleUpdate = async () => {

        try {
            setLoading(true)
            const { data } = await ApiService.updateUser(editingUser.id, {
                name: formData.name,
                email: formData.email,
                phone: formData.phone,
                gender: formData.dob,
                dob: formData.dob,
                image: formData.image,
                address: {
                    line1: formData.address && formData.address.line1,
                    line2: formData.address && formData.address.line2
                }
            })

            if (data.success) {
                toast.success(data.message)
                getUsers()
            } else {
                toast(data.message)
            }

            closeModal();

        } catch (error) {
            console.log(error)
            toast.error(error.response ? error.response.data.message : 'something went wrong')
        } finally {
            setLoading(false)
        }
    }

    const handleChange = (e) => {
        setFormData(prev => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    }

    const closeModal = () => {
        setEditingUser(null);
        setIsModalOpen(false);
    }

    useEffect(() => {

        if (token) {
            getUsers()
        }

    }, [page, maxPagesToShow, sortDirection, sortField])

    return (
        <>
            <LoadWrapper loading={loading}>
                {
                    users &&
                    <div className="p-6 max-w-8xl mx-auto">
                        <h1 className="text-3xl font-bold mb-6">Admin - Manage Users</h1>

                        <div className="bg-white shadow-md rounded-lg p-4">
                            <table className="w-full table-auto">
                                <thead>
                                    <tr className="bg-gray-100">
                                        <th onClick={() => { sortDirection === 'ASC' ? setSortDirection('DESC') : setSortDirection('ASC'); setSortField('id'); }} className="p-3 text-left cursor-pointer">ID {sortField === 'id' ? sortDirection === 'ASC' ? downArrow : upArrow : ''}</th>
                                        <th className="p-3 text-left">Image</th>
                                        <th onClick={() => { sortDirection === 'ASC' ? setSortDirection('DESC') : setSortDirection('ASC'); setSortField('username'); }} className="p-3 text-left cursor-pointer">Username {sortField === 'username' ? sortDirection === 'ASC' ? downArrow : upArrow : ''}</th>
                                        <th onClick={() => { sortDirection === 'ASC' ? setSortDirection('DESC') : setSortDirection('ASC'); setSortField('email'); }} className="p-3 text-left cursor-pointer">Email {sortField === 'email' ? sortDirection === 'ASC' ? downArrow : upArrow : ''}</th>
                                        <th onClick={() => { sortDirection === 'ASC' ? setSortDirection('DESC') : setSortDirection('ASC'); setSortField('name'); }} className="p-3 text-left cursor-pointer">Name {sortField === 'name' ? sortDirection === 'ASC' ? downArrow : upArrow : ''}</th>
                                        <th onClick={() => { sortDirection === 'ASC' ? setSortDirection('DESC') : setSortDirection('ASC'); setSortField('dob'); }} className="p-3 text-left cursor-pointer">DOB {sortField === 'dob' ? sortDirection === 'ASC' ? downArrow : upArrow : ''}</th>
                                        <th onClick={() => { sortDirection === 'ASC' ? setSortDirection('DESC') : setSortDirection('ASC'); setSortField('gender'); }} className="p-3 text-left cursor-pointer">Gender {sortField === 'gender' ? sortDirection === 'ASC' ? downArrow : upArrow : ''}</th>
                                        <th onClick={() => { sortDirection === 'ASC' ? setSortDirection('DESC') : setSortDirection('ASC'); setSortField('phone'); }} className="p-3 text-left cursor-pointer">Phone {sortField === 'phone' ? sortDirection === 'ASC' ? downArrow : upArrow : ''}</th>
                                        <th className="p-3 text-left cursor-pointer">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {users.map((user, index) => (
                                        <tr key={user.id} className={index < 9 ? 'border-gray-300 border-b' : ''}>
                                            <td className="p-3">{user.id}</td>
                                            <td className="p-3">
                                                <img src={user.image}
                                                    alt="Profile"
                                                    className="w-10 h-10 rounded-full object-cover" />
                                            </td>
                                            <td className="p-3">{user.username}</td>
                                            <td className="p-3">{user.email}</td>
                                            <td className="p-3">{user.name}</td>
                                            <td className="p-3">{user.dob}</td>
                                            <td className="p-3">{user.gender}</td>
                                            <td className="p-3">{user.phone}</td>
                                            <td className="p-3 flex gap-2">
                                                <button
                                                    className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                                                    onClick={() => handleEdit(user)}
                                                >
                                                    Edit
                                                </button>
                                                <button
                                                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"

                                                >
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>

                            {/* ******************************* Pagination ******************************* */}
                            <Pagination maxPagesToShow={maxPagesToShow} totalPage={totalPage} page={page} setPage={setPage} />

                        </div>

                        {/* === Modal === */}
                        {isModalOpen && (
                            <div className="fixed inset-0 bg-gray-100 bg-opacity-50 flex items-center justify-center z-50">
                                <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                                    <h2 className="text-2xl font-semibold mb-4">Edit User</h2>
                                    <div className="flex flex-col gap-4">
                                        <input type="text" name="name" placeholder="Name" value={formData.name} onChange={handleChange} className="border rounded p-2" />
                                        <input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} className="border rounded p-2" />
                                        <input type="text" name="username" placeholder="Username" value={formData.username} onChange={handleChange} className="border rounded p-2" />
                                        <input type="text" name="name" placeholder="name" value={formData.name} onChange={handleChange} className="border rounded p-2" />
                                        <input type="date" name="dob" placeholder="Date Of Birth" value={formData.dob} onChange={handleChange} className="border rounded p-2" />
                                        <input type="number" name="phone" placeholder="Phone" value={formData.phone} onChange={handleChange} className="border rounded p-2" />

                                        <select onChange={handleChange} name="gender" className="border rounded p-2">
                                            <option value="Male">Male</option>
                                            <option value="Female">Female</option>
                                        </select>
                                        <div className="flex gap-4 mt-4">
                                            <button
                                                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 w-full"
                                                onClick={handleUpdate}
                                            >
                                                Update
                                            </button>
                                            <button
                                                className="bg-gray-400 text-white px-4 py-2 rounded hover:bg-gray-500 w-full"
                                                onClick={closeModal}
                                            >
                                                Cancel
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                }
            </LoadWrapper>
        </>)
}

export default UserList