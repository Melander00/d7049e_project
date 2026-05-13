accum = 0
interval = 2

function update(dt)

end

function fixedUpdate(dt)
    accum = accum + dt
    if accum > interval then
        accum = 0
        local rigidbody = context:getPhysics().rigidBody
        local v3 = context:createVec3()
        v3.y = 5
        rigidbody:setLinearVelocity(v3)
    end
end